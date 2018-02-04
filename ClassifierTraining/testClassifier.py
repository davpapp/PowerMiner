import numpy as np
import cv2

willowCascade = cv2.CascadeClassifier('/home/dpapp/open/opencv-haar-classifier-training/classifier/stage9.xml')
img = cv2.imread('/home/dpapp/Desktop/RunescapeAIPics/CascadeTraining/Testing/screenshot0.png')
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

willows = willowCascade.detectMultiScale(gray, 1.3, 5)
for (x,y,w,h) in willows:
    cv2.rectangle(img,(x,y),(x+w,y+h),(255,0,0),2)
    roi_gray = gray[y:y+h, x:x+w]
    roi_color = img[y:y+h, x:x+w]
    print("Found willow!")

cv2.imshow('img', img)
cv2.waitKey(0)
cv2.destroyAllWindows()
