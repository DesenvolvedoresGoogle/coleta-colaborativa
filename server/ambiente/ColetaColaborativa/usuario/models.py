#coding:utf-8

from django.db import models

# Create your models here.
class Usuario(models.Model):
    nome = models.CharField('Nome', max_length=100)
    telefone = models.CharField('Telefone', max_length=20)
    email = models.CharField('Email', max_length=100)

    class Meta:
        verbose_name = u'Usuário'
        verbose_name_plural = u'Usuários'

    def __unicode__(self):
        return self.nome + '-' + self.telefone
