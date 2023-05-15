'''
Notas para utilizar: 
O fundo está no git
Largura de banda = 130 (alterar na nuvem do core)

'''
f = open("../movesAcident.scen", "w")

cityes_positinions_Towers = {
    "Esq" : (100, 355),
    "Dir" : (857, 362),
}
cityes_positinions_car = {
    "AMB1" : (100, 442),
    "AMB2" : (467, 420),
    "AMB3" : (430, 545),
    "AMB4" : (102, 548),
    "N16_1" : (248, 460),
    "N16_2" : (100, 449),
    "N14" : (50, 530),
# Mais perto da torre e recebe acidente
    "N17_1" : (654, 400),
    "N17_2" : (950, 421),
# Deve ignorar mensagem
    "N18_1" : (654, 479),
    "N18_2" : (950, 478),
# Fica quase parada e tem acidente
    "N19_Ini" : (704, 530),
    "N19_1" : (789, 531),
    "N19_2" : (714, 531),
    "Fora" : (37, 700),

}

initial_positions_tower = {
    12: "Esq",
    13: "Dir"
}

initial_positions = {
   14: "Fora",
    15:"Fora" ,
    16: "Fora",
    17: "N17_1",
    18:"N18_1" ,
    19:"N19_Ini" ,
}


time_fim_simulation = 40

time_init_move_acident = 1
time_chega_move_acident = 5

time_acident = 9

moves = {
    17: {
        2 :( "N17_1", 15),
        time_init_move_acident :( "N17_1", 25),
        time_chega_move_acident :( "N17_2", 25),
        time_fim_simulation :( "N17_2", 25),
    },

    18: {
        2 :( "N18_1", 15),
        time_init_move_acident :( "N18_1", 25),
        time_chega_move_acident :( "N18_2", 25),
        time_fim_simulation :( "N18_2", 25),
    },
    19: {
        2 :( "N19_1", 15),
        time_acident :( "N19_2", 25),
        time_fim_simulation :( "N19_2", 25),
    }
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
temp ="Acident_Conf" 
file1Car ="../carros/src/Car/Acident_Conf" 
file2Car ="../carros/out/artifacts/carros_jar/Acident_Conf" 

# There can only be one action per position per car.
actions = [
    ("n19","N19_2", "break"),
    ("n19","N19_2", "accident"),
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

