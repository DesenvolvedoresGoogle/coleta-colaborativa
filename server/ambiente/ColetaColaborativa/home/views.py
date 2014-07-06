#coding:utf-8
from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
import random

from pontos.models import Tipo
from pontos.models import Ponto
from pontos.models import Estatistica

# Create your views here.
@csrf_exempt
def index(request):
    pontos = Ponto.objects.all()
    print pontos
    return render(request, 'home/index.html', {'pontos' : pontos})

def no_mapa(request):
    estatisticas = Estatistica.objects.all()
    return render(request, 'home/no_mapa.html', {'estatisticas': estatisticas})

@csrf_exempt
def estatistica(request):
    # Recupera as estatísticas de coleta.
    tipos = Tipo.objects.all()
    dadosUtilizacao = []
    for  tipo in tipos:
        estatisticas = Estatistica.objects.filter(tipo_descarte__id = tipo.id, tipo = 2)
        if len(estatisticas) > 0:
            cor = getCorHexadecimal()
            item = ItemGraficoPizza(valor = len(estatisticas), color = cor, highlight = cor, label = tipo.nome)
            dadosUtilizacao.append(item)
    
    dadosConsultados = []
    for tipo in tipos:
        estatisticas = Estatistica.objects.filter(tipo_descarte__id = tipo.id, tipo = 1)
        if len(estatisticas) > 0:
            cor = getCorHexadecimal()
            item = ItemGraficoPizza(valor = len(estatisticas), color = cor, highlight = cor, label = tipo.nome)
            dadosConsultados.append(item) 
   
    return render(request, 'home/estatistica.html', {'dadosUtilizacao': dadosUtilizacao, 'dadosConsultados': dadosConsultados})

def getCorHexadecimal():
    r = lambda: random.randint(0,255)
    return '#%02X%02X%02X' % (r(),r(),r())

class ItemGraficoPizza:
    valor, color, highlight, label = 0, '', '', ''

    def __init__(self, valor, color, highlight, label):
        self.valor = valor
        self.color = color
        self.highlight = highlight
        self.label = label

class ItemGraficoBarra:
    descricao = ''

    def __init__(self, descricao):
        self.descricao = descricao

class ValorItemGraficoBarra:
    valor = 0

    def __init__(self, valor):
        self.valor = valor
