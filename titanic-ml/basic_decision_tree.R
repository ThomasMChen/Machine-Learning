#Load Dependencies
library(rpart)
library(rpart.plot)
library(rattle)

# Import training set: train
train <- read.csv("data/train.csv")
# Import testing set: test
test <- read.csv("data/test.csv")

# Build decision tree
test_tree <- rpart(Survived ~ Pclass + Sex + Age + SibSp + Parch + Fare + Embarked, data = train, method = "class")

# Visualize the decision tree using rpart.plot fancyplot function
quartz()
fancyRpartPlot(test_tree)

# Predictions based on decision tree
prediction <- predict(test_tree, newdata = test, type = "class")

# Save solution into dataframe
solution <- data.frame(PassengerId = test$PassengerId, Survived = prediction)


# Check solution has exactly 418 rows, write solution to .csv
if (nrow(solution) == 418) {
  write.csv(solution, file = "solutions/solution.csv", row.names = FALSE)
  print("Passed")
} else {
  print("Failed")
}


# # More Features
# 
# test_tree_two <- rpart(Survived ~ Pclass + Sex + Age + SibSp + Parch + Fare + Embarked,
#                        data = train, method = "class", control = rpart.control(minsplit = 100, cp = 0))
# 
# # Visualize my_tree_three
# fancyRpartPlot(test_tree_two)
# 
# # Predictions based on decision tree
# prediction <- predict(test_tree_two, newdata = test, type = "class")
# 
# # Save solution into dataframe
# solution <- data.frame(PassengerId = test$PassengerId, Survived = prediction)
# 
# # Check solution has exactly 418 rows, write solution to .csv
# if (nrow(solution) == 418) {
#   write.csv(solution, file = "solutions/solution-adv.csv", row.names = FALSE)
#   print("Passed")
# } else {
#   print("Failed")
# }

