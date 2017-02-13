#Load Dependencies
library(rpart)
library(rpart.plot)
library(rattle)

# Import training set: train
train <- read.csv("data/train.csv")
# Import testing set: test
test <- read.csv("data/test.csv")

# Feature Engineering Title using Regex
test_new <- test
test_new$Title <- sub("^.*?,","",test$Name)
test_new$Title <- sub("\\..*","",test_new$Title)
test_new$title <- sub(" ","",test_new$Title)
test_new$Title 

train_new <-  train
train_new$Title <- sub("^.*?,","",train$Name)
train_new$Title <- sub("\\..*","",train_new$Title)
train_new$Title <- sub(" ","",train_new$Title)
train_new$Title 

# Check for Parity between Training and Testing Data
factor(test_new$Title)
factor(train_new$Title)

# Build Decision Tree
tree_fe <- rpart(Survived ~ Pclass + Sex + Age + SibSp + Parch + Fare + Embarked + Title,
                      data = train_new, method = "class")

# Visualize tree with new Title Feature
quartz()
fancyRpartPlot(tree_fe)

# Make prediction and solution
prediction <- predict(tree_fe, test_new, type = "class")
solution <- data.frame(PassengerId = test_new$PassengerId, Survived = prediction)


# Check solution has exactly 418 rows, write solution to .csv
if (nrow(solution) == 418) {
  write.csv(solution, file = "solutions/solution_fe.csv", row.names = FALSE)
  print("Passed")
} else {
  print("Failed")
}