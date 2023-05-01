'''
Notas para utilizar: 
O fundo está no git
Largura de banda = 110 (alterar na nuvem do core)

'''
f = open("movesPortugal.scen", "w")

cityes_positinions_Towers = {
    "VC" : (252, 397),
    "Porto" : (300, 633),
    "Braga2" : (761, 360),
}
cityes_positinions_car = {
    "VC" : (276, 414),
    "Porto" : (306, 670),
    "Braga" : (386, 480),
    "Braga2" : (739, 406),
    "VR" : (530, 530),  # Vila Real
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
        7 :( "Porto", 15),
        20 :( "VR", 25),
        30 :( "Braga2", 25),
        45 :( "VC", 25),
    },

    16: {
        2 :( "VC", 15),
        13 :( "Braga", 15),
        31 :( "Braga2", 25),
    },

    17: {
        2 :( "Porto", 15),
        12 :( "Braga2", 25),
        27 :( "VC", 15),
    },

    18: {
        2 :( "Porto", 15),
        12 : ("VC", 15),
        27 : ("Braga2", 25),
    },
    19: {
        2 : ("Braga2", 25),
        12 : ("Porto", 25),
        27 : ("VC", 17),
    },
}

def get_pos_city(city_name):
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

# Ambulance part

ambulance = 15
import os
'''
def read_file(file_name):
    file_handle = open(file_name, "w+")
    file_handle.write("#Time;x,y")
    for time, (city_name,vel) in moves[ambulance].items():
        (x,y) = get_pos_city(city_name)
        file_handle.write(f"{time};{x},{y}")
    file_handle.close()

# f_amb = open("./src/Car/mov_amb.txt", "w")
'''
from os import path

#file_path = path.relpath("src/Car/mov_amb.txt", "w")
#with open("src/Car/mov_amb.txt", "w") as f2:
file1 ="./carros/src/Car/amb_moves" 
file2 ="./carros/out/artifacts/carros_jar/amb_moves" 
def write_ambs(file_path): 
    with open(file_path, "w") as f2:
        f2.write("#Time;x,y\n")
        for time, (city_name,vel) in moves[ambulance].items():
            (x,y) = get_pos_city(city_name)
            f2.write(f"{time};{x},{y};\n")
        f2.close()

write_ambs(file1)
write_ambs(file2)
'''
cur_dir = os.path.dirname(os.path.realpath('__file__'))
file_name = os.path.join(cur_dir, 'src/Car/mov_amb.txt')
'''
