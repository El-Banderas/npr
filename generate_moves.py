
f = open("moves1.scen", "w")

initial_positions = {
    14: (110, 450, 0),
    15: (110, 500, 0),
    16: (110, 600, 0),
    17: (855, 450, 0),
    18: (855, 500, 0),
    19: (855, 600, 0),
}

moves = {
    14: {
        2 : (150, 450, 5),
        5 : (855.0 ,450.0 ,25.0),
    },

    15: {
        2 : (150, 500.0, 5),
        7 : (855.0 ,500.0 ,25.0),
    },
    16: {
        2 : (150, 600.0, 5),
        7 : (855.0 ,600.0 ,35.0),
    },

    17: {
        2 : (830.0, 450.0 ,5.0),
        7 : (100.0, 450.0 ,25.0),
    },

    18: {
        2 : (810.0, 500.0 ,5.0),
        7 : (115.0, 500.0 ,35.0),
    },
    19: {
        2 : (810.0, 600.0 ,5.0),
        7 : (100.0, 600.0 ,35.0),
    },
}

for car, (x,y,z) in initial_positions.items():
    f.write(f"$node_({car}) set X_ {x}\n")
    f.write(f"$node_({car}) set Y_ {y}\n")
    f.write(f"$node_({car}) set Z_ {z}\n")

for car, car_info in moves.items():
    print(car_info)
    for time, (x,y,vel) in car_info.items():
        f.write(f"$ns_ at {time} \"$node_({car}) setdest {x} {y} {vel}\"\n")

f.close()