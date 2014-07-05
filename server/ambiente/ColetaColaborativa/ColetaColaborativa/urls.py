from django.conf.urls import patterns, include, url

from pontos import views as views_pontos

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'ColetaColaborativa.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),

    url(r'novo_ponto/', views_pontos.novo_ponto),
    url(r'novo_local/', views_pontos.novo_local)
)
