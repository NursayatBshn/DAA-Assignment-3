import csv
import os
import sys

# Добавляем пути к модулям из кэша uv, если matplotlib еще не в sys.path
try:
    import matplotlib.pyplot as plt
except ImportError:
    base = os.path.expanduser('~/.cache/uv/archive-v0')
    if os.path.exists(base):
        for d in os.listdir(base):
            p = os.path.join(base, d)
            if os.path.isdir(p):
                sys.path.insert(0, p)
    import matplotlib.pyplot as plt

from collections import defaultdict

out_dir = os.path.abspath('results/plots')
os.makedirs(out_dir, exist_ok=True)

# 1. Чтение results.csv
data = defaultdict(lambda: defaultdict(list))
with open('results/results.csv', 'r') as f:
    reader = csv.DictReader(f)
    for row in reader:
        algo = row['algorithm']
        graph = row['graph']
        density = 'sparse' if 'sparse' in graph else 'dense'
        n = int(row['n'])
        m = int(row['m'])
        dsu_ops = int(row['dsu_ops'])
        edge_scans = int(row['edge_scans'])
        comparisons = int(row['comparisons'])
        max_depth = int(row['max_depth'])
        result_val = int(row['result'])
        time_ms = float(row['time_ms'])

        total_ops = dsu_ops + edge_scans + comparisons

        data[algo][density].append({
            'n': n,
            'm': m,
            'dsu_ops': dsu_ops,
            'edge_scans': edge_scans,
            'comparisons': comparisons,
            'total_ops': total_ops,
            'time_ms': time_ms,
            'result': result_val
        })

# Сортировка по n
for algo in data:
    for density in data[algo]:
        data[algo][density].sort(key=lambda x: x['n'])

colors = {
    'Kruskal': '#1f77b4',
    'Prim': '#ff7f0e',
    'Kosaraju': '#2ca02c',
    'Kahn': '#d62728',
    'DagShortestPath': '#9467bd'
}

# --- График 1: Operations vs n (log-log axes) ---
plt.figure(figsize=(10, 6))
for algo, densities in data.items():
    color = colors.get(algo, 'black')
    if 'sparse' in densities:
        xs = [p['n'] for p in densities['sparse']]
        ys = [p['total_ops'] for p in densities['sparse']]
        plt.plot(xs, ys, marker='o', linestyle='-', color=color, label=f"{algo} (sparse, m=4n)")
    if 'dense' in densities:
        xs = [p['n'] for p in densities['dense']]
        ys = [p['total_ops'] for p in densities['dense']]
        plt.plot(xs, ys, marker='s', linestyle='--', color=color, alpha=0.7, label=f"{algo} (dense, m=20n)")

plt.xscale('log')
plt.yscale('log')
plt.xlabel('n (Number of Vertices)', fontsize=12)
plt.ylabel('Total Operations (log scale)', fontsize=12)
plt.title('Plot 1: Operations vs n (Log-Log Scale)', fontsize=14, fontweight='bold')
plt.grid(True, which="both", ls="--", alpha=0.5)
plt.legend(bbox_to_anchor=(1.04, 1), loc="upper left", fontsize=9)
plt.tight_layout()
p1 = os.path.join(out_dir, 'plot1_operations_vs_n.png')
plt.savefig(p1, dpi=300)
plt.close()

# --- График 2: Scans per edge (edgeScans / m vs n) ---
plt.figure(figsize=(9, 5.5))
for algo in ['Kosaraju', 'Kahn', 'DagShortestPath']:
    color = colors.get(algo, 'black')
    for density, style in [('sparse', '-'), ('dense', '--')]:
        if density in data[algo]:
            xs = [p['n'] for p in data[algo][density]]
            ys = [p['edge_scans'] / p['m'] for p in data[algo][density]]
            plt.plot(xs, ys, marker='o' if style == '-' else 's', linestyle=style,
                     color=color, label=f"{algo} ({density})")

plt.xlabel('n (Number of Vertices)', fontsize=12)
plt.ylabel('Scans per Edge (edgeScans / m)', fontsize=12)
plt.title('Plot 2: Scans per Edge vs n (Confirmation of Linear Work)', fontsize=14, fontweight='bold')
plt.ylim(0, 2.5)
plt.grid(True, which="both", ls="--", alpha=0.5)
plt.legend(loc='center right', fontsize=10)
plt.tight_layout()
p2 = os.path.join(out_dir, 'plot2_scans_per_edge.png')
plt.savefig(p2, dpi=300)
plt.close()

# --- График 3: Share of edges Kruskal examines (edgeScans / m) ---
plt.figure(figsize=(8.5, 5))
if 'Kruskal' in data:
    for density, style, color, marker in [('sparse', '-', '#1f77b4', 'o'), ('dense', '--', '#ff7f0e', 's')]:
        if density in data['Kruskal']:
            xs = [p['n'] for p in data['Kruskal'][density]]
            ys = [p['edge_scans'] / p['m'] for p in data['Kruskal'][density]]
            label = f"Kruskal ({density}, m={4 if density == 'sparse' else 20}n)"
            plt.plot(xs, ys, marker=marker, linestyle=style, color=color, linewidth=2, label=label)

plt.xlabel('n (Number of Vertices)', fontsize=12)
plt.ylabel('Share of Edges Examined (edgeScans / m)', fontsize=12)
plt.title('Plot 3: Share of Edges Kruskal Examines (Sparse vs Dense)', fontsize=14, fontweight='bold')
plt.grid(True, which="both", ls="--", alpha=0.5)
plt.ylim(0, 1.05)
plt.legend(loc='center right', fontsize=11)
plt.tight_layout()
p3 = os.path.join(out_dir, 'plot3_kruskal_examined_share.png')
plt.savefig(p3, dpi=300)
plt.close()

print("All 3 plots generated successfully in results/plots/!")
