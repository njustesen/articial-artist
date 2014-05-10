set term post eps size 4.5,3 font 20

set border 4095 lw 2

set yrange [0:1]

set xlabel "Iteration"
set ylabel "Best fitness"

set key bottom right

set output '/home/niels/Documents/articial-artist/results/resemblance.eps'

plot "/home/niels/Documents/articial-artist/results/existing-mating.dat" using 1:2 w lines t "Mating", \
"/home/niels/Documents/articial-artist/results/existing-mutation.dat" using 1:2 w lines t "Mutation", \
"/home/niels/Documents/articial-artist/results/existing-neat.dat" using 1:2 w lines t "JNEAT Epoch"

