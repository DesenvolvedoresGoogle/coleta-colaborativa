#coding:utf-8
from django.views.decorators.csrf import csrf_exempt

from django.shortcuts import render
from django.http import HttpResponse

from usuario.models import Usuario
from pontos.models import Ponto
from pontos.models import Tipo
from pontos.models import Estatistica

import json
import haversine

# Create your views here.
@csrf_exempt
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

    #ponto.save()

    #for tipo in dados['tipos']:
    #    tipo_banco = Tipo.objects.get(id=tipo['id'])
    #    ponto.tipos.add(tipo_banco)

    return HttpResponse('Hello Novo Ponto')

'''
Método que responde com um JSON com todos os Tipos de Descartes disponíveis.
'''
@csrf_exempt
def consulta_todos_tipos(request):
    tipos = Tipo.objects.all()
    lista_dicionario_tipos = []

    for tipo in tipos:
        lista_dicionario_tipos.append(tipo.get_dicionario())

    response = {
        'response' : 1,
        'tipos' : lista_dicionario_tipos
    }

    return HttpResponse(json.dumps(response))


'''
Método que responde com um JSON com todos os Pontos de Descartes disponíveis.
'''
@csrf_exempt
def consulta_todos_pontos(request):
    pontos = Ponto.objects.all()
    lista_dicionario_pontos = []

    for ponto in pontos:
        lista_dicionario_pontos.append(ponto.get_dicionario())

    response = {
        'response' : 1,
        'pontos' : lista_dicionario_pontos
    }

    return HttpResponse(json.dumps(response))
'''
Método que espera um POST com os dados da localização atual do usuário
e procura no banco de dados o ponto mais próximo do usuário.
'''
@csrf_exempt
def consulta_pontos_proximos_tipos(request):
    if request.method == 'POST':
        id_tipo = request.POST['tipo_id']
        latitude = request.POST['latitude']
        longitude = request.POST['longitude']

        pontos = Ponto.objects.filter(tipos__id=id_tipo)

        melhor_distacia = None
        melhor_ponto = None

        for ponto in pontos:
            distancia = haversine.haversine(float(latitude), float(longitude), float(ponto.latitude), float(ponto.longitude))

            if melhor_distacia == None:
                melhor_distacia = distancia
                melhor_ponto = ponto
                continue

            if melhor_distacia > distancia:
                melhor_distacia = distancia
                melhor_ponto = ponto

        tipo = Tipo.objects.get(id=id_tipo)

        estatistica = Estatistica()
        estatistica.ponto = ponto
        estatistica.latitude = latitude
        estatistica.longitude = longitude
        estatistica.tipo = 1
        estatistica.tipo_descarte = tipo
        estatistica.save()

        response = {
            'response' : 1,
            'ponto' : ponto.get_dicionario()
        }
    else:
        response = {
                'response' : -1,
            }

    return HttpResponse(json.dumps(response))

'''
Método que retorna para a tela os pontos próximos do usuário.
O limite de pontos para serem retornados é 50.
'''
@csrf_exempt
def consulta_pontos_proximos(request):
    if request.method == 'POST':
        latitude = request.POST['latitude']
        longitude = request.POST['longitude']

        pontos = Ponto.objects.all()
        pontos_aptos = []
        contador = 0
        for ponto in pontos:
            distancia = haversine.haversine(float(latitude), float(longitude), float(ponto.latitude), float(ponto.longitude))

            if distancia < 3000:
                pontos_aptos.append(ponto.get_dicionario())
                contador = contador + 1

            if contador == 50:
                break

        response = {
            'response' : 1,
            'pontos' : pontos_aptos
        }

    else:
        response = {
                'response' : -1,
            }

    return HttpResponse(json.dumps(response))




def create_json_novo_ponto():
    dados_client = {
        'latitude' : 98.787878,
        'longitude' : 46.98927,
        'ponto_privado' : False,
        'email' : 'email@email.com',
        'nome' : 'Usuario 001',
        'id' : '1234567890',
        'url' : 'http://www.google.com',
        'descricao' : 'descricao',
        'tipos' : [{'id' : 1}, {'id' : 2}] }

    return dados_client
