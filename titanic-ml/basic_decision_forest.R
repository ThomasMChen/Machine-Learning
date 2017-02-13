#Load Dependencies
library('rpart') # decision tree
library('rpart.plot') # visualization
library('rattle') # visualization
library('ggplot2') # visualization
library('ggthemes') # visualization
library('scales') # visualization
library('dplyr') # data manipulation
library('mice') # imputation
library('randomForest') # classification algorithm'

# Import training set: train
train <- read.csv("data/train.csv")
# Import testing set: test
test <- read.csv("data/test.csv")

# Feature Engineering Title using Regex
test_new <- test
test_new$Title <- sub("^.*?,","",test$Name)
test_new$Title <- sub("\\..*","",test_new$Title)
test_new$Title <- sub(" ","",test_new$Title)
test_new$Title 

train_new <-  train
train_new$Title <- sub("^.*?,","",train$Name)
train_new$Title <- sub("\\..*","",train_new$Title)
train_new$Title <- sub(" ","",train_new$Title)
train_new$Title 

test <- test_new
train <- train_new

# Combine into single data frame
all_data  <- bind_rows(train, test) # bind training & test data

# Create a family size variable including passenger
all_data$family_size <- full$SibSp + full$Parch + 1

# Create a family variable based on surname
all_data$Family <- paste(full$Surname, full$Fsize, sep='_')

# Passenger on row 62 and 830 do not have a value for embarkment, assume SouthHampton (most common)
all_data$Embarked[c(62, 830)] <- "S"

# Factorize embarkment codes.
all_data$Embarked <- factor(all_data$Embarked)

# Passenger on row 1044 has an NA Fare value. Let's replace it with the median fare value.
all_data$Fare[1044] <- median(all_data$Fare, na.rm = TRUE)

# Use decision tree to fill missing age
predicted_age <- rpart(Age ~ Pclass + Sex + SibSp + Parch + Fare + Embarked + Title + family_size,
                       data = all_data[!is.na(all_data$Age),], method = "anova")
all_data$Age[is.na(all_data$Age)] <- predict(predicted_age, all_data[is.na(all_data$Age),])

#Set Title from char to factor
all_data$Title <- as.factor(all_data$Title)

# Split the data back into a train set and a test set
train <- all_data[1:891,]
test <- all_data[892:1309,]

# train and test are available in the workspace
str(train)
str(test)

# Apply the Random Forest Algorithm
my_forest <- randomForest(as.factor(Survived) ~ Pclass + Sex + Age + SibSp + Parch + Fare + Embarked + Title, data = train, importance = TRUE, ntree = 5000)
prediction <- predict(my_forest, test, type="class")
my_solution <- data.frame(PassengerId = test$PassengerId, Survived = prediction)
write.csv(my_solution, file = "solutions/solution_forest.csv", row.names=FALSE)

# Important Variable Charts
quartz()
varImpPlot(my_forest)

#Check vs Kaggle Benchmark
kaggle <- read.csv("solutions/kaggle_forest.csv")
solutions <- data.frame(as.numeric(my_solution$Survived), as.numeric(kaggle$Survived))
solutions$gen <- solutions$as.numeric.my_solution.Survived.
solutions$kag <- solutions$as.numeric.kaggle.Survived.
solutions <- subset(solutions, select = -c(as.numeric.my_solution.Survived., as.numeric.kaggle.Survived. ))

solutions$gen[solutions$gen==1] <- 0
solutions$gen[solutions$gen==2] <- 1

solutions$diff = solutions$gen - solutions$kag

differences <- colSums(solutions != 0)
differences