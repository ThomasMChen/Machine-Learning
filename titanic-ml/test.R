#Load Dependencies
library(rpart)
library(rpart.plot)
library(rattle)

# Import training set: train
train <- read.csv("train.csv")

# Import testing set: test
test <- read.csv("test.csv")

#View Data
str(train)
str(test)

# table(train$Survived)
# prop.table(train$Survived)
# table(train$Sex, train$Survived)
# prop.table(table(train$Sex, train$Survived), 1)
# train$Child <- NA
# train$Child[train$Age < 18] <- "Child"
# train$Child[train$Age >= 18] <- "Adult"
# prop.table(table(train$Child, train$Survived), 1)

# Copy of test
test_one <- test

# Initialize a Survived column to 0
test_one$Survived <- 0

# Set Survived to 1 if Sex equals "female"
test_one$Survived[test_one$Sex == "female"] <- 1


# Build the decision tree
test_tree <- rpart(Survived ~ Pclass + Sex + Age + SibSp + Parch + Fare + Embarked, data = train, method = "class")

# Visualize the decision tree using plot() and text()
quartz()
#plot(test_tree)
#text(test_tree)
prp(test_tree, varlen=4)
quartz()
fancyRpartPlot(test_tree)

# Make predictions on the test set
prediction <- predict(test_tree, newdata = test, type = "class")

# Finish the data.frame() call
solution <- data.frame(PassengerId = test$PassengerId, Survived = prediction)

# Use nrow() on my_solution
nrow(my_solution)

# Finish the write.csv() call
write.csv(solution, file = "solution.csv", row.names = FALSE)
