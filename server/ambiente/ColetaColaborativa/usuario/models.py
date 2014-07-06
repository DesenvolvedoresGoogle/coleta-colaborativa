#coding:utf-8

from django.db import models

# Create your models here.
class Usuario(models.Model):
    nome = models.CharField('Nome', max_length=100)
    email = models.CharField('Email', max_length=100)
    id_login = models.CharField('ID Login', max_length=100)
    url = models.CharField('URL', max_length=100)

    class Meta:
        verbose_name = u'Usuário'
        verbose_name_plural = u'Usuários'

    def __unicode__(self):
        return self.nome

    def get_dicionario(self):
        retorno = {
            'nome' : self.nome,
            'email' : self.email
        }

        return retorno
