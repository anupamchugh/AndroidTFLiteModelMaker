#from __future__ import absolute_import, division, print_function, unicode_literals
import tensorflow as tf

from tensorflow_examples.lite.model_maker.core.data_util.image_dataloader import ImageClassifierDataLoader
from tensorflow_examples.lite.model_maker.core.task import image_classifier
from tensorflow_examples.lite.model_maker.core.task.model_spec import ImageModelSpec


data = ImageClassifierDataLoader.from_folder('nsfw/')
train_data, test_data = data.split(0.8)
model = image_classifier.create(train_data)
loss, accuracy = model.evaluate(test_data)
model.export('nsfw_classifier.tflite', 'nsfw_label.txt')
