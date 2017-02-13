# Load packages
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

full  <- bind_rows(train, test) # bind training & test data

#Check for missing values
apply(full,2,function(x) sum(is.na(x)))

# Passengers 62 and 830 are missing Embarkment
full[c(62, 830), 'Embarked']

# Get rid of our missing passenger IDs
embark_fare <- full %>%
  filter(PassengerId != 62 & PassengerId != 830)

# Use ggplot2 to visualize embarkment, passenger class, & median fare
ggplot(embark_fare, aes(x = Embarked, y = Fare, fill = factor(Pclass))) +
  geom_boxplot() +
  geom_hline(aes(yintercept=80), 
             colour='red', linetype='dashed', lwd=2) +
  scale_y_continuous(labels=dollar_format()) +
  theme_few()

# Since their fare was $80 for 1st class, they most likely embarked from 'C'
full$Embarked[c(62, 830)] <- 'C'

# Show row 1044
full[1044, ]

ggplot(full[full$Pclass == '3' & full$Embarked == 'S', ], 
       aes(x = Fare)) +
  geom_density(fill = '#99d6ff', alpha=0.4) + 
  geom_vline(aes(xintercept=median(Fare, na.rm=T)),
             colour='red', linetype='dashed', lwd=1) +
  scale_x_continuous(labels=dollar_format()) +
  theme_few()

# Replace missing fare value with median fare for class/embarkment
full$Fare[1044] <- median(full[full$Pclass == '3' & full$Embarked == 'S', ]$Fare, na.rm = TRUE)

# Show number of missing Age values
sum(is.na(full$Age))

# Make variables factors into factors
factor_vars <- c('PassengerId','Pclass','Sex','Embarked',
                 'Title','Surname','Family','FsizeD')

full[factor_vars] <- lapply(full[factor_vars], function(x) as.factor(x))

# Set a random seed
set.seed(129)

# Perform mice imputation, excluding certain less-than-useful variables:
mice_mod <- mice(full[, !names(full) %in% c('PassengerId','Name','Ticket','Cabin','Family','Surname','Survived')], method='rf') 

# Save the complete output 
mice_output <- complete(mice_mod)

# Plot age distributions
quartz()
par(mfrow=c(1,2))
hist(full$Age, freq=F, main='Age: Original Data', 
     col='darkgreen', ylim=c(0,0.04))
hist(mice_output$Age, freq=F, main='Age: MICE Output', 
     col='lightgreen', ylim=c(0,0.04))

# Replace Age variable from the mice model.
full$Age <- mice_output$Age

# Show new number of missing Age values
sum(is.na(full$Age))

apply(full,2,function(x) sum(is.na(x)))

# Split the data back into a train set and a test set
train <- full[1:891,]
test <- full[892:1309,]

# head(train)
# 
# quartz()
# ggplot(train, aes(Fare, Age , color = Sex)) + geom_point()
# 
# set.seed(20)
# cluster <- kmeans(train[,c("Age","Fare")], 2, nstart = 20)
# cluster
# 
# train$cluster <- cluster$cluster
# table(train$cluster, train$Survived)
# 
# quartz()
# cluster$cluster <- as.factor(cluster$cluster)
# ggplot(train, aes(train$Age, train$Fare, color = train$cluster)) + geom_point()

set.seed(20)
cluster <- kmeans(test[,c("Age", "Fare", "SibSp", "Parch")], 2, nstart=20)
cluster

test$cluster <- cluster$cluster
ans <- test[c("PassengerId", "cluster")]

ans[ans==1] <- 0
ans[ans==2] <- 1
ans

colnames(ans)[2] <- "Survived"

# Check solution has exactly 418 rows, write solution to .csv
if (nrow(ans) == 418) {
  write.csv(ans, file = "solutions/kmeans-test.csv", row.names = FALSE)
  print("Passed")
} else {
  print("Failed")
}
