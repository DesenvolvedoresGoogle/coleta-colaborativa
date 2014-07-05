#coding:utf-8

from django.db import models

class Tipo(models.Model):
    nome = models.CharField('Tipo de Coleta', max_length=200)

'''
Model que mapeia um Ponto de Coleta.
'''
class Ponto(models.Model):
    latitude = models.DecimalField('Latitude', max_digits=10, decimal_places=8)
    longitude = models.DecimalField('Longitude', max_digits=10, decimal_places=8)
    ponto_privado = models.BooleanField('Ponto Privado', default=False)
    descricao = models.TextField('Descrição')

    class Meta:
        verbose_name = u'Ponto'
        verbose_name_plural = u'Pontos'

    def __unicode__(self):
        return self.descricao[0:50] + '...'

'''
Model que mapeia um Local de Coleta.
'''
class Local(models.Model):
    observacao = models.TextField('Observações')

    #Chave para o model Tipo.
    tipo = models.ForeignKey(Tipo)

    class Meta:
        verbose_name = u'Local de Coleta'
        verbose_name_plural = u'Locais de Coleta'

    def __unicode__(self):
        return self.tipo