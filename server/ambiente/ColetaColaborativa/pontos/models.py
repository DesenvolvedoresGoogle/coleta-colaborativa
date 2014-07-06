#coding:utf-8

from django.db import models

from usuario.models import Usuario

'''
Model que mapeia um Tipo de Coleta.
'''
class Tipo(models.Model):
    nome = models.CharField('Tipo de Coleta', max_length=200)
    cor = models.CharField('Cor', max_length=30, choices= (
        ('azul', 'azul' ),
        ('vermelho', 'vermelho' ),
        ('amarelo', 'amarelo' ),
        ('verde', 'verde' ),
        ('cinza', 'cinza' ),
        ('prata', 'prata' ),
        ('dourado', 'dourado' ),
            ))

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
    tipos = models.ManyToManyField(Tipo)

    class Meta:
        verbose_name = u'Ponto'
        verbose_name_plural = u'Pontos'

    def __unicode__(self):
        return self.descricao[0:50] + '...'

    def get_dicionario(self):
        retorno = {
            'id' : str(self.id),
            'latitude' : str(self.latitude),
            'longitude' : str(self.longitude),
            'ponto_privado' : self.ponto_privado,
            'descricao' : self.descricao,
            'usuario' : self.usuario.get_dicionario(),
        }

        retorno['tipos'] = []
        for tipo in self.tipos.iterator():
            retorno['tipos'].append(tipo.get_dicionario())
        
        return retorno

'''
Model que mapeia as Estatisticas geradas pelo sistema.
'''
class Estatistica(models.Model):
    ponto = models.ForeignKey(Ponto)
    hora = models.DateTimeField(auto_now=True)
    latitude = models.DecimalField('Latitude', max_digits=10, decimal_places=8)
    longitude = models.DecimalField('Longitude', max_digits=10, decimal_places=8)
    tipo = models.SmallIntegerField() #1 - Consulta / 2 - Descarte

    tipo_descarte = models.ForeignKey(Tipo)

    class Meta:
        verbose_name = u'Estatísica'
        verbose_name_plural = u'Estatísicas'