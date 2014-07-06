from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

from pontos.models import Ponto

# Create your views here.
@csrf_exempt
def index(request):
    pontos = Ponto.objects.all()

    return render(request, 'home/index.html', {'pontos' : pontos})
