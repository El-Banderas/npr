'''
Notas para utilizar: 
O fundo está no git
Largura de banda = 100 (alterar na nuvem do core)

'''
f = open("../movesPortugal.scen", "w")

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
    "N19_1" : (789, 531),
    "N19_2" : (858, 531),
}

initial_positions_tower = {
    12: "Esq",
    13: "Dir"
}

initial_positions = {
   14: "N14",
    15:"AMB1" ,
    16: "N16_1",
    17: "N17_1",
    18:"N18_1" ,
    19:"N19_1" ,
}

time_init_move_amb = 5
time_chega_move_amb = 25
time_fim_simulation = 40

time_init_move_acident = 5
time_chega_move_acident = 25

time_acident = 24

moves = {
    #14: {
    #    2 : (150, 450, 5),
    #    5 : (855.0 ,450.0 ,25.0),
    #},
    15: {
        2 :( "AMB1", 15),
        time_init_move_amb :( "AMB2", 15),
        time_chega_move_acident :( "AMB3", 25),
        time_fim_simulation :( "AMB3", 25),
    },

    16: {
        2 :( "N16_1", 15),
        time_init_move_amb :( "N16_1", 25),
        time_chega_move_acident :( "N16_2", 25),
        time_fim_simulation :( "N16_2", 25),
    },

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

temp ="Tower_Pos_generates" 
file1Car ="../carros/src/Car/Tower_Pos_generates" 
file2Car ="../carros/out/artifacts/carros_jar/Tower_Pos_generates" 

# There can only be one action per position per car.
actions = [
    ("n19","N19_2", "break"),
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
'''
cur_dir = os.path.dirname(os.path.realpath('__file__'))
file_name = os.path.join(cur_dir, 'src/Car/mov_amb.txt')
'''
