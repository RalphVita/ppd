import pandas as pd
import matplotlib.pyplot as plt

dfResponse = pd.read_csv('compare.csv')



#Segundos
dfResponse['Tempo de Resposta'] = dfResponse['Tempo de Resposta']/1000



#---------Tempo de Resposta -> Cenário A------------
fig, ax = plt.subplots(1,2)

dfResponseA = dfResponse[dfResponse["Cenário"] == "A"]

for key, grp in dfResponseA.groupby(['Trabalho']):
        ax[0].plot(grp['Tamanho Vetor']/1000, grp['Tempo de Resposta'], label=key)

ax[0].set_title('Cenário A')
ax[0].set_ylabel('Tempo de Resposta (s)')
ax[0].set_xlabel('Tamanho Vetor (Kb)')
ax[0].legend(title="Versão")


#---------Tempo de Resposta -> Cenário B------------

dfResponseA = dfResponse[dfResponse["Cenário"] == "B"]

for key, grp in dfResponseA.groupby(['Trabalho']):
        ax[1].plot(grp['Tamanho Vetor']/1000, grp['Tempo de Resposta'], label=key)

ax[1].set_title('Cenário B')
ax[1].set_ylabel('Tempo de Resposta (s)')
ax[1].set_xlabel('Tamanho Vetor (Kb)')
ax[1].legend(title="Versão")
plt.show()

# #---------Tempo de Resposta -> Cenário A------------
# fig, ax = plt.subplots()

# dfResponseB = dfResponse[dfResponse["Cenário"] == "B"]

# for key, grp in dfResponseB.groupby(['Cenário']):
#     for key, grp in grp.groupby(['Granularidade m']):
#         ax.plot(grp['Tamanho Vetor']/1000, grp['Tempo de Resposta'], label=key)

# ax.set_title('Cenário B')
# ax.set_ylabel('Tempo de Resposta (s)')
# ax.set_xlabel('Tamanho Vetor (Kb)')
# ax.legend(title="m")
# plt.show()




