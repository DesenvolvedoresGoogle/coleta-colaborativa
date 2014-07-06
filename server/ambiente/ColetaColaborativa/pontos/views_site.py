#coding:utf-8
from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

from usuario.models import Usuario
from pontos.models import Ponto

@csrf_exempt
def index(request):
    if request.method == 'POST':
        return processa_post_novo(request)
    else:
        return render(request, 'pontos/index.html', {})

def view_novo_ponto(request):
    return render(request, 'pontos/novo_ponto.html')

def processa_post_novo(request):
    #TRATAR DADOS VINDOS DO REQUEST.
    dados = request.POST
    #Verifica se o usuário já existe.
    print dados

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
