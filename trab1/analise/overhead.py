import pandas as pd
import matplotlib.pyplot as plt

#dfResponse = pd.read_csv('temporesposta.csv')
dfOverhead = pd.read_csv('overhead.csv',names=['p','Tamanho Vetor','Overhead'])

fig, ax = plt.subplots()

# dfOverhead.groupby('p').plot(x='Tamanho Vetor', y='Overhead',ax=ax,legend=True)
#dfOverhead.groupby('p')['Overhead'].plot(x='Tamanho Vetor',legend=True)

for key, grp in dfOverhead.groupby(['p']):
    ax.plot(grp['Tamanho Vetor']/1000, grp['Overhead'], label=key)

ax.set_title('Tamanho Vetor x Overhead x p')
ax.set_ylabel('Overhead (ms)')
ax.set_xlabel('Tamanho Vetor (Kb)')
ax.legend()
plt.show()