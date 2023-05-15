'''
Notas para utilizar: 
O fundo está no git
Largura de banda = 130 (alterar na nuvem do core)

'''
f = open("../movesAmb.scen", "w")

cityes_positinions_Towers = {
    "Esq" : (100, 355),
    "Dir" : (857, 362),
}
cityes_positinions_car = {
    "AMB1" : (96, 534),
    "AMB2" : (856, 549),
    "N16_1" : (98, 442),
    "N16_2" : (859, 455),
    "N14" : (916, 552),
   "Fora" : (37, 700),
}

initial_positions_tower = {
    12: "Esq",
    13: "Dir"
}

initial_positions = {
   14: "N14",
    15:"AMB1" ,
    16: "N16_1",
    17: "Fora",
    18:"Fora" ,
    19:"Fora" ,
}

time_init_move_amb = 5
time_amb_1 = 2
time_amb_2 = 6
time_chega_move_amb = 28
time_fim_simulation1 = 32
time_fim_simulation = 35

time_init_move_acident = 5
time_chega_move_acident = 25

time_acident = 28

moves = {
    #14: {
    #    2 : (150, 450, 5),
    #    5 : (855.0 ,450.0 ,25.0),
    #},
    15: {
        time_amb_1 :( "AMB1", 35),
        time_amb_2 :( "AMB2", 35),
        time_fim_simulation1 :( "AMB2", 35),
        time_fim_simulation :( "AMB2", 35),
    },

    16: {
        time_amb_1 :( "N16_1", 35),
        time_amb_2 :( "N16_2", 35),
        time_fim_simulation :( "N16_2", 35),
    },

    
}

def get_pos_city_tower(city_name):
    (x,y) = cityes_positinions_Towers[city_name]
    return (x,y)

for tower, (city_name) in initial_positions_tower.items():
    (x,y) = get_pos_city_tower(city_name)
    z = 0
    f.write(f"$node_({tower}) set X_ {x}\n")
    f.write(f"$node_({tower}) set Y_ {y}\n")
    f.write(f"$node_({tower}) set Z_ {z}\n")

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

from os import path

#file_path = path.relpath("src/Car/mov_amb.txt", "w")
#with open("src/Car/mov_amb.txt", "w") as f2:
file1 ="../carros/src/Car/amb_moves" 
file2 ="../carros/out/artifacts/carros_jar/amb_moves" 
def write_ambs(file_path): 
    with open(file_path, "w") as f2:
        f2.write("#Time;x,y\n")
        for time, (city_name,vel) in moves[ambulance].items():
            (x,y) = get_pos_city(city_name)
            f2.write(f"{time};{x},{y};\n")
        f2.close()

write_ambs(file1)
write_ambs(file2)

temp ="Amb_Conf" 
file1Car ="../carros/src/Car/Amb_Conf" 
file2Car ="../carros/out/artifacts/carros_jar/Amb_Conf" 

# There can only be one action per position per car.
actions = [
]

def write_Car_config(file_path): 
    with open(file_path, "w") as f2:
        f2.write("Nome;PosX,PosY;\n")
        for city_name, (x,y) in cityes_positinions_Towers.items():
            f2.write(f"{city_name};{x},{y};\n")
        f2.write("Node;PosX,PosY;action;\n")
        for (node_name, pos, action) in actions:
            (x,y) = get_pos_city(pos)
            f2.write(f"{node_name};{x},{y};{action};\n")
        f2.close()

write_Car_config(temp)
write_Car_config(file1Car)
write_Car_config(file2Car)
