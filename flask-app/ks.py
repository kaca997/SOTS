import pandas as pd
import numpy as np
import sys
sys.path.append('learning_spaces/')
from learning_spaces.kst import iita


from flask import Flask
from flask_restful import Resource, Api

app = Flask(__name__)
api = Api(app)

class CreateKS(Resource):
    def get(self):
        data_frame = load_data("pisa.txt")
        list = []
        response = iita(data_frame, v=1)
        print(response)

        for a,b  in response["implications"]:
            list.append({"orderNum1" : a,
                        "orderNum2" : b})

        
        return list

api.add_resource(CreateKS, '/getRealKS')


def load_data(filePath): 
    data = pd.read_fwf(filePath, sep=" ", header=None)
    del data[0]

    data.columns = data.iloc[0]
    data = data.reindex(data.index.drop(0)).reset_index(drop=True)
    data.columns.name = None
    data_ = data.astype(int)
    
    print(data_)

    return data_
if __name__ == '__main__':
    app.run(debug=True)