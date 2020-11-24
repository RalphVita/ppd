import pandas as pd
import matplotlib.pyplot as plt

dfResponse = pd.read_csv('temporesposta.csv')

#Segundos
dfResponse['Tempo de Resposta'] = dfResponse['Tempo de Resposta']/1000

#Spped Up
x=list(dfResponse['Tempo de Resposta'][0:4])*8
dfResponse['Speed Up'] = x/dfResponse['Tempo de Resposta']

#Eficiência
dfResponse['Eficiência'] = dfResponse['Speed Up']/dfResponse['Nº Máquinas']



#---------Eficiência------------
fig, ax = plt.subplots()


for key, grp in dfResponse.groupby(['Tamanho Vetor']):
    ax.plot(grp['Nº Máquinas'], grp['Speed Up'], label=key/1000)

ax.set_title('Speed Up x Nº Máquinas x Tamanho Vetor (Kb)')
ax.set_ylabel('Speed Up')
ax.set_xlabel('Nº Máquinas')
ax.legend()
plt.show()



#---------speed Up------------
fig, ax = plt.subplots()


for key, grp in dfResponse.groupby(['Tamanho Vetor']):
    ax.plot(grp['Nº Máquinas'], grp['Eficiência'], label=key/1000)

ax.set_title('Eficiência x Nº Máquinas x Tamanho Vetor (Kb)')
ax.set_ylabel('Eficiência (%)')
ax.set_xlabel('Nº Máquinas')
ax.legend()
plt.show()

#---------Tempo de Resposta------------
fig, ax = plt.subplots()


for key, grp in dfResponse.groupby(['Nº Máquinas']):
    ax.plot(grp['Tamanho Vetor']/1000, grp['Tempo de Resposta'], label=key)

ax.set_title('Tempo de Resposta x Tamanho Vetor x Nº Máquinas')
ax.set_ylabel('Tempo de Resposta (s)')
ax.set_xlabel('Tamanho Vetor (Kb)')
ax.legend()
plt.show()


