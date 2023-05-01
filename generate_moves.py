
f = open("movesPortugal.scen", "w")

cityes_positinions_Towers = {
    "VC" : (252, 397),
    "Porto" : (300, 633),
    "Braga2" : (761, 360),
}
cityes_positinions_car = {
    "VC" : (276, 414),
    "Porto" : (306, 670),
    "Braga2" : (739, 406),
}

initial_positions = {
 #   14: (110, 450, 0),
    15:"VC" ,
    16: "VC",
    17: "Porto",
    18:"Porto" ,
    19:"Braga2" ,
}

moves = {
    #14: {
    #    2 : (150, 450, 5),
    #    5 : (855.0 ,450.0 ,25.0),
    #},
    15: {
        2 :( "Porto", 15),
        7 :( "Braga2", 25),
    },

    16: {
        3 :( "Porto", 15),
        9 :( "Braga2", 25),
    },

    17: {
        2 :( "Braga2", 25),
        7 :( "VC", 15),
    },

    18: {
        2 : ("VC", 15),
        7 : ("Braga2", 25),
    },
    19: {
        2 : ("Porto", 25),
        7 : ("VC", 17),
    },
}

def get_pos_city(city_name):
    print(city_name)
    thing = cityes_positinions_car[city_name]
    print(thing)
    (x,y) = cityes_positinions_car[city_name]
    return (x,y)

for car, (city_name) in initial_positions.items():
    (x,y) = get_pos_city(city_name)
    z = 0
    f.write(f"$node_({car}) set X_ {x}\n")
    f.write(f"$node_({car}) set Y_ {y}\n")
    f.write(f"$node_({car}) set Z_ {z}\n")

for car, car_info in moves.items():
    print(car_info)
    for time, (city_name,vel) in car_info.items():
        (x,y) = get_pos_city(city_name)
        f.write(f"$ns_ at {time} \"$node_({car}) setdest {x} {y} {vel}\"\n")

f.close()