#coding:utf-8

from django.db import models

from usuario.models import Usuario

'''
Model que mapeia um Tipo de Coleta.
'''
class Tipo(models.Model):
    nome = models.CharField('Tipo de Coleta', max_length=200)

    class Meta:
        verbose_name = u'Tipo'
        verbose_name_plural = u'Tipos'

    def __unicode__(self):
        return self.nome

    def get_dicionario(self):
        retorno = {
            'nome' : self.nome,
            'id' : self.id
        }

        return retorno

'''
Model que mapeia um Ponto de Coleta.
'''
class Ponto(models.Model):
    latitude = models.DecimalField('Latitude', max_digits=10, decimal_places=8)
    longitude = models.DecimalField('Longitude', max_digits=10, decimal_places=8)
    ponto_privado = models.BooleanField('Ponto Privado', default=False)
    descricao = models.TextField('Descrição')

    #Associação entre ponto e usuário que cadastrou o ponto.
    usuario = models.ForeignKey(Usuario)

    class Meta:
        verbose_name = u'Ponto'
        verbose_name_plural = u'Pontos'

    def __unicode__(self):
        return self.descricao[0:50] + '...'

    def get_dicionario(self):
        lista_dicionarios_locais = []

        try:
            locais = Local.objects.filter(ponto__id=self.id)

            if len(locais) > 0:
                for local in locais.values():
                    lista_dicionarios_locais.append(local)

        except Local.DoesNotExist:
            pass

        retorno = {
            'latitude' : str(self.latitude),
            'longitude' : str(self.longitude),
            'ponto_privado' : self.ponto_privado,
            'descricao' : self.descricao,
            'usuario' : self.usuario.get_dicionario()
        }

        if len(lista_dicionarios_locais) != 0:
            retorno['locais'] = lista_dicionarios_locais

        return retorno

'''
Model que mapeia um Local de Coleta.
'''
class Local(models.Model):
    observacao = models.TextField('Observações')

    #Chave para o model Tipo.
    tipo = models.ForeignKey(Tipo)

    #Chave para o model Ponto.
    ponto = models.ForeignKey(Ponto)

    class Meta:
        verbose_name = u'Local de Coleta'
        verbose_name_plural = u'Locais de Coleta'

    def __unicode__(self):
        return self.tipo

    def get_dicionario(self):
        retorno ={
            'tipo' : self.tipo.nome,
            'observacao' : self.observacao
        }

        return retorno