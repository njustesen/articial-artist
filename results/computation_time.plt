set term post eps size 4.5,3 font 20

# set style histogram errorbars gap 2 lt -1 lw 2

set border 4095 lw 2

# set yrange [0:1]
set xrange [0:700]

set ylabel "Paint time (ms)"
set xlabel "Resolution"

set key bottom right

set output '/home/niels/Documents/articial-artist/results/computation.eps'

plot "/home/niels/Documents/articial-artist/results/computation_time.dat" using 1:2:3 with errorbars t "150x150", \
"" using 4:5:6 with errorbars t "300x300", \
"" using 7:8:9 with errorbars t "600x600"