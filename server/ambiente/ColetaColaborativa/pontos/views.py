#coding:utf-8
from django.shortcuts import render
from django.http import HttpResponse

from usuario.models import Usuario
from pontos.models import Ponto
from pontos.models import Local
from pontos.models import Tipo

import json

# Create your views here.
def novo_ponto(request):
    #TRATAR DADOS VINDOS DO REQUEST.

    dados = create_json_novo_ponto()

    #Verifica se o usuário já existe.
    try:
        usuario = Usuario.objects.get(id_login=dados['id'])
    except Usuario.DoesNotExist: # Se o usuário não existir, um novo é criado.
        usuario = Usuario()
        usuario.nome = dados['nome']
        usuario.id_login = dados['id']
        usuario.url = dados['url']
        usuario.email = dados['email']
        usuario.save()

    #Cria um ponto com os dados vindo da tela e associação com o usuario.
    ponto = Ponto()
    ponto.latitude = dados['latitude']
    ponto.longitude = dados['longitude']
    ponto.descricao = dados['descricao']
    ponto.ponto_privado = dados['ponto_privado']
    ponto.usuario = usuario
    ponto.save()

    return HttpResponse('Hello Novo Ponto')

def novo_local(request):
    #TRATAR DADOS VINDOS DO REQUEST.
    dados = create_json_novo_local()

    #Recupera a referencia do tipo no banco de dados.
    try:
        tipo = Tipo.objects.get(id=dados['tipo'])
    except Tipo.DoesNotExist:
        return HttpResponse('ERRO')    

    #Recupera a referencia do ponto no banco de dados.
    try:
        ponto = Ponto.objects.get(id=dados['ponto'])
    except Ponto.DoesNotExist:
        return HttpResponse('ERRO')     

    local = Local()
    local.tipo = tipo
    local.ponto = ponto
    local.observacoes = dados['observacoes']
    local.save()

    return HttpResponse('Hello Novo Local') 


def create_json_novo_ponto():
    dados_client = {
        'latitude' : 98.787878,
        'longitude' : 46.98927,
        'ponto_privado' : False,
        'email' : 'email@email.com',
        'nome' : 'Usuario 001',
        'id' : '1234567890',
        'url' : 'http://www.google.com',
        'descricao' : 'descricao' }

    return dados_client

def create_json_novo_local():
    dados_local = {
        'tipo' : 1,
        'observacoes' : 'Observações',
        'ponto' : 1
    }

    return dados_local
