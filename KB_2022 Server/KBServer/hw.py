#! C:\Users\Lee\anaconda3\python.exe
print("content-type: text/html; charset=utf-8\n")

import tensorflow as tf
import numpy as np
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import load_img
from tensorflow.keras.preprocessing.image import img_to_array
import os
import cgi
import cgitb
import json

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

cgi_ptr = cgi.FieldStorage()
userid = cgi_ptr.getvalue('userID');
filename = ""#예측할 이미지경로
model = tf.keras.models.load_model("")#Trash_Classification.h5 가중치 파일 불러오기(경로 입력)

img_shape = 224
label_names = ['종이박스', '유리', '캔', '종이', '플라스틱', '일반쓰레기']
original = load_img(filename, target_size = (224,224))
numpy_image = img_to_array(original)
image_batch = np.expand_dims(numpy_image , axis = 0)
percentage = model.predict(image_batch)#예측 실행(확률로 나타냄)
predict = np.argmax(percentage);#확률중에서 가장 큰 값을 return
#print(percentage[0][predict])#예측 class에 대한 확률 나타내기
if(percentage[0][predict] * 100 >= 50):#확률이 50퍼센트 이상이라면
    result_object = {
        "percent" : round(percentage[0][predict] * 100, 1),
        "result" : label_names[predict]
    }
else:
    result_object = {
        "percent" : 0,
        "result" : "fail"
    }

result_json = json.dumps(result_object)
print(result_json)
