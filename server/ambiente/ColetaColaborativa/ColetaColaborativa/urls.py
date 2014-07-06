from django.conf.urls import patterns, include, url

from pontos import views as views_pontos
from pontos import views_site
from home import views as views_home

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'ColetaColaborativa.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),

    url(r'novo_ponto/', views_pontos.novo_ponto),
    url(r'consulta_tipos/', views_pontos.consulta_todos_tipos),
    url(r'consulta_pontos/', views_pontos.consulta_todos_pontos),
    url(r'consulta_ponto_proximo_tipo/', views_pontos.consulta_pontos_proximos_tipos),
    url(r'consulta_ponto_proximo/', views_pontos.consulta_pontos_proximos),

    url(r'^$', views_home.index, name='index'),

    url(r'view/pontos/', views_site.index, name='pontos'),
    url(r'view/no_mapa/', views_site.no_mapa, name='no_mapa')
)
