import torch
import torch.nn as nn
import torch.nn.init as init
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from sklearn.datasets import make_regression
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score
import torch.nn.functional as F

# Set the number of input features and output features
inFeatures = 1
outFeaturesReg = 1
outFeaturesClass = 1

torch.manual_seed(42)  # Set a seed for PyTorch
np.random.seed(42)  # Set a seed for NumPy

# Define the neural network model with no hidden layers (linear regression)
class SimpleLinearRegression(nn.Module):
    def __init__(self, in_features=1, out_features=outFeaturesReg):
        super(SimpleLinearRegression,self).__init__()
        self.out = nn.Linear(in_features, out_features)  # A single linear layer for regression

    def forward(self, x):
        return self.out(x)  # No activation function, just linear output
class classificationWithRelu(nn.Module):
    def __init__(self, in_features=inFeatures, h1 = 1, out_features=outFeaturesClass):
        super(classificationWithRelu,self).__init__()
        self.fc1= nn.Linear(in_features,h1)
        self.out = nn.Linear(h1, out_features)  # A single linear layer for regression

    def forward(self, x):
        x = F.relu(self.fc1(x))
        x = F.sigmoid(self.out(x))
        return x

class SimpleLinearRegressionClassification(nn.Module):
    def __init__(self, in_features=inFeatures, out_features=outFeaturesClass):
        super(SimpleLinearRegressionClassification,self).__init__()
        self.out = nn.Linear(in_features, out_features)  # A single linear layer for regression

    def forward(self, x):
        x=self.out(x)
        return F.sigmoid(x)

# Generate dataset with noise
X =np.random.rand(10000,1).astype(np.float32)
y= 2*X+1+0.01*np.random.randn(10000,1).astype(np.float32)
#generate 2d data
num_samples = 10000
w = np.random.uniform(-20, 20, num_samples).astype(np.float32)
z = np.random.uniform(-20, 20, num_samples).astype(np.float32)

# Labels: 1 if above the line (y > 2x + 1), 0 otherwise
labels = (z > 2 * w+ 1).astype(np.float32)
labels2 = ((z<5*w-11) & (z>(-4)*w+5)).astype(np.float32)
data = np.stack((w, z), axis=1)
# Create feature column names
feature_columns = [f'feature_{i+1}' for i in range(1)]
df = pd.DataFrame(X, columns=feature_columns)
y_classification = (y > (2*X+1)).astype(np.int64)
colors = ['red' if value ==1 else 'blue' for value in labels]
colors2 = ['red' if value ==1 else 'blue' for value in labels2]

plt.figure(figsize=(10, 6))
plt.plot(w,2*w+1, color='green', label='decision line 1')
plt.scatter(w, z, c=colors, alpha=0.6, label='linear regression')
plt.show()

plt.figure(figsize=(10, 6))
plt.plot(w,5*w-11, color='green', label='decision line 1')
plt.plot(w,(-4)*w+5, color='green', label='decision line 2')
plt.scatter(w, z, c=colors2, alpha=0.6, label='linear regression')
plt.show()

# Train-test split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
X_trainClass, X_testClass, y_trainClass, y_testClass,y_trainClass2, y_testClass2, z_trainClass, z_testClass = train_test_split(w, labels,labels2, z, test_size=0.2, random_state=42)

# Linear Regression model for comparison
linmodel = LinearRegression()
linmodel.fit(X_train, y_train)
y_pred = linmodel.predict(X_test)

# Neural Network model for regression
model = SimpleLinearRegression()
model2 = SimpleLinearRegressionClassification()
model3= classificationWithRelu()
model22 = SimpleLinearRegressionClassification()
model32= classificationWithRelu()
#to debug weights do [p for p in model.parameters()] in debug consol or model.state_dict()
# Convert to tensors
a_train = torch.FloatTensor(X_train)
a_test = torch.FloatTensor(X_test)
b_train = torch.FloatTensor(y_train)
b_test = torch.FloatTensor(y_test)

X_train_clf = torch.FloatTensor(X_trainClass).unsqueeze(1)
X_test_clf = torch.FloatTensor(X_testClass).unsqueeze(1)
y_train_clf = torch.FloatTensor(y_trainClass).unsqueeze(1)
y_test_clf = torch.FloatTensor(y_testClass).unsqueeze(1)

X_train_clf_relu = torch.FloatTensor(X_trainClass).unsqueeze(1)
X_test_clf_relu = torch.FloatTensor(X_testClass).unsqueeze(1)
y_train_clf_relu = torch.FloatTensor(y_trainClass).unsqueeze(1)
y_test_clf_relu = torch.FloatTensor(y_testClass).unsqueeze(1)

y_train_clf2 = torch.FloatTensor(y_trainClass2).unsqueeze(1)
y_test_clf2 = torch.FloatTensor(y_testClass2).unsqueeze(1)

y_train_clf_relu2 = torch.FloatTensor(y_trainClass2).unsqueeze(1)
y_test_clf_relu2 = torch.FloatTensor(y_testClass2).unsqueeze(1)
# Set criterion and optimizer
criterion = nn.MSELoss()  # Mean Squared Error for regression
optimizer = torch.optim.Adam(model.parameters(), lr=0.01)  # Optimizer

criterion_clf = nn.BCELoss()
optimizer_clf = torch.optim.Adam(model2.parameters(), lr=0.01)

criterion_clf_relu = nn.BCELoss()
optimizer_clf_relu = torch.optim.Adam(model3.parameters(), lr=0.01)

criterion_clf2 = nn.BCELoss()
optimizer_clf2 = torch.optim.Adam(model22.parameters(), lr=0.01)

criterion_clf_relu2 = nn.BCELoss()
optimizer_clf_relu2 = torch.optim.Adam(model32.parameters(), lr=0.01)

# Training loop
epochs = 1000  # Number of epochs for training

for i in range(epochs):
    # Forward pass
    b_pred = model.forward(a_train)
    y_pred_clf = model2.forward(X_train_clf)
    y_pred_clf_relu = model3.forward(X_train_clf_relu)
    y_pred_clf2 = model22.forward(X_train_clf)
    y_pred_clf_relu2 = model32.forward(X_train_clf_relu)

    # Compute error
    error = criterion(b_pred, b_train)
    error_clf = criterion_clf(y_pred_clf, y_train_clf)
    error_clf_relu = criterion_clf_relu(y_pred_clf_relu, y_train_clf_relu)
    error_clf2 = criterion_clf2(y_pred_clf2, y_train_clf2)
    error_clf_relu2 = criterion_clf_relu2(y_pred_clf_relu2, y_train_clf_relu2)


    # Backpropagation
    optimizer.zero_grad()
    error.backward()
    optimizer.step()
    
    optimizer_clf.zero_grad()
    error_clf.backward()
    optimizer_clf.step()

    optimizer_clf_relu.zero_grad()
    error_clf_relu.backward()
    optimizer_clf_relu.step()

    optimizer_clf2.zero_grad()
    error_clf2.backward()
    optimizer_clf2.step()

    optimizer_clf_relu2.zero_grad()
    error_clf_relu2.backward()
    optimizer_clf_relu2.step()

# Prediction with the trained neural network
with torch.no_grad():
    b_eval = model(a_test)
    y_eval_clf = model2.forward(X_test_clf)
    y_eval_clf_relu = model3.forward(X_test_clf_relu)
    y_eval_clf2 = model22.forward(X_test_clf)
    y_eval_clf_relu2 = model32.forward(X_test_clf_relu)
    
y_pred_class = (y_eval_clf > 0.5).float()
correct = (y_pred_class == y_test_clf).sum().item()
accuracy = correct / len(y_test_clf) * 100
print(f"Test Accuracy: {accuracy:.2f}%")

y_pred_class_relu = (y_eval_clf_relu > 0.5).float()
correct_relu = (y_pred_class_relu == y_test_clf_relu).sum().item()
accuracy_relu = correct_relu / len(y_test_clf_relu) * 100
print(f"Test Accuracy Relu: {accuracy_relu:.2f}%")

y_pred_class2 = (y_eval_clf2 > 0.5).float()
correct2 = (y_pred_class2 == y_test_clf2).sum().item()
accuracy2 = correct2 / len(y_test_clf2) * 100
print(f"Test Accuracy 2: {accuracy2:.2f}%")

y_pred_class_relu2 = (y_eval_clf_relu2 > 0.5).float()
correct_relu2 = (y_pred_class_relu2 == y_test_clf_relu2).sum().item()
accuracy_relu2 = correct_relu2 / len(y_test_clf_relu2) * 100
print(f"Test Accuracy Relu2: {accuracy_relu2:.2f}%")


#Plotting comparison between models
plt.figure(figsize=(10, 6))
plt.scatter(y_test, y_pred, color='blue', alpha=0.6, label='Linear Regression Predictions')
plt.scatter(b_test, b_eval, color='green', alpha=0.6, label='Neural Network Predictions')

# Plot settings
plt.xlabel('True Values')
plt.ylabel('Predicted Values')
plt.title('Comparison of Regression Predictions')
plt.legend()
plt.show()

#plotting the clf 
colors_pred = ['red' if pred == 1 else 'blue' for pred in y_pred_class.flatten().tolist()]
colors_pred2 = ['red' if pred == 1 else 'blue' for pred in y_pred_class2.flatten().tolist()]
colors_pred_relu = ['red' if pred == 1 else 'blue' for pred in y_pred_class_relu.flatten().tolist()]
colors_pred_relu2 = ['red' if pred == 1 else 'blue' for pred in y_pred_class_relu2.flatten().tolist()]

#plot model 1
plt.scatter(X_testClass, z_testClass, c=colors_pred, alpha=0.6, label='Predicted Classification')
plt.plot(w,2*w+1, color='green', label='Original Regression Line')

# Add labels and legend
plt.xlabel('Feature (X)')
plt.ylabel('Target (y)')
plt.title('Predicted Classification')
plt.legend()
plt.show()

colorss = ['purple' if value ==1 else 'green' for value in y_testClass.flatten()]
plt.scatter(X_testClass, z_testClass, c=colorss, alpha=0.6, label='actual classification')
plt.plot(w,2*w+1, color='green', label='Original Regression Line')
plt.legend()
plt.show()


#plot model relu1
plt.scatter(X_testClass, z_testClass, c=colors_pred_relu, alpha=0.6, label='Predicted Classification relu')
plt.plot(w,2*w+1, color='green', label='Original Regression Line')

# Add labels and legend
plt.xlabel('Feature (X)')
plt.ylabel('Target (y)')
plt.title('Predicted Classification relu')
plt.legend()
plt.show()

plt.scatter(X_testClass, z_testClass, c=colorss, alpha=0.6, label='actual classification relu')
plt.plot(w,2*w+1, color='green', label='Original Regression Line')
plt.legend()
plt.show()


#plot model 2
plt.scatter(X_testClass, z_testClass, c=colors_pred2, alpha=0.6, label='Predicted Classification 2')
plt.plot(w,5*w-11, color='green', label='decision line 1')
plt.plot(w,(-4)*w+5, color='green', label='decision line 2')
# Add labels and legend
plt.xlabel('Feature (X)')
plt.ylabel('Target (y)')
plt.title('Predicted Classification 2')
plt.legend()
plt.show()

colorss2 = ['purple' if value ==1 else 'green' for value in y_testClass2.flatten()]
plt.scatter(X_testClass, z_testClass, c=colorss2, alpha=0.6, label='actual classification 2')
plt.plot(w,5*w-11, color='green', label='decision line 1')
plt.plot(w,(-4)*w+5, color='green', label='decision line 2')
plt.legend()
plt.show()

#plot model relu2
plt.scatter(X_testClass, z_testClass, c=colors_pred_relu2, alpha=0.6, label='Predicted Classification relu2')
plt.plot(w,5*w-11, color='green', label='decision line 1')
plt.plot(w,(-4)*w+5, color='green', label='decision line 2')
# Add labels and legend
plt.xlabel('Feature (X)')
plt.ylabel('Target (y)')
plt.title('Predicted Classification relu2')
plt.legend()
plt.show()

plt.scatter(X_testClass, z_testClass, c=colorss2, alpha=0.6, label='actual classification relu2')
plt.plot(w,5*w-11, color='green', label='decision line 1')
plt.plot(w,(-4)*w+5, color='green', label='decision line 2')
plt.legend()
plt.show()