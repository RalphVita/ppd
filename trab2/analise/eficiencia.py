import pandas as pd
import matplotlib.pyplot as plt

dfResponse = pd.read_csv('temporesposta.csv')



#Segundos
dfResponse['Tempo de Resposta'] = dfResponse['Tempo de Resposta']/1000



#---------Tempo de Resposta -> Cenário A------------
fig, ax = plt.subplots()

dfResponseA = dfResponse[dfResponse["Cenário"] == "A"]

for key, grp in dfResponseA.groupby(['Cenário']):
    for key, grp in grp.groupby(['Granularidade m']):
        ax.plot(grp['Tamanho Vetor']/1000, grp['Tempo de Resposta'], label=key)

ax.set_title('Cenário A')
ax.set_ylabel('Tempo de Resposta (s)')
ax.set_xlabel('Tamanho Vetor (Kb)')
ax.legend(title="m")
plt.show()

#---------Tempo de Resposta -> Cenário A------------
fig, ax = plt.subplots()

dfResponseB = dfResponse[dfResponse["Cenário"] == "B"]

for key, grp in dfResponseB.groupby(['Cenário']):
    for key, grp in grp.groupby(['Granularidade m']):
        ax.plot(grp['Tamanho Vetor']/1000, grp['Tempo de Resposta'], label=key)

ax.set_title('Cenário B')
ax.set_ylabel('Tempo de Resposta (s)')
ax.set_xlabel('Tamanho Vetor (Kb)')
ax.legend(title="m")
plt.show()



fig, ax = plt.subplots()

dfResponseA = dfResponseA[dfResponseA["Granularidade m"] == 20000]
dfResponseB = dfResponseB[dfResponseB["Granularidade m"] == 20000]

ax.plot(dfResponseA['Tamanho Vetor']/1000, dfResponseA['Tempo de Resposta'],"--", label="A")
ax.plot(dfResponseB['Tamanho Vetor']/1000, dfResponseB['Tempo de Resposta'], label="B")

ax.set_title('Cenário A X B para m = 1000')
ax.set_ylabel('Tempo de Resposta (s)')
ax.set_xlabel('Tamanho Vetor (Kb)')
ax.legend(title="Cenário")
plt.show()


