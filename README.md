# NullPointerException

<p align="center">
  <img src="https://raw.githubusercontent.com/ykoziy/NullPointerException_p2/main/logo.png" />
</p>

## Project Description

NullPointerException is an online PC component store created using Spring Boot for the back end and HTML/CSS/JavaScript for the front end.

## Technologies Used

- Spring Boot
- Java 1.8
- HTML/CSS/JavaScript
- AWS Cloud
  - PostgreSQL RDS
  - AWS Elastic Beanstalk (back-end deployment)
  - Amazon S3 (front-end deployment)
- Unit testing with test reports
- Aspect Logging
- Docker

## Features

- Password hashing, user passwords are not stored as plain text
- User is able to log in
- User is able to create a new account
- User is able to view and edit his profile
- User is able to see all product availabe
- User can add/remove items to/from shopping cart
- User is able to checkout with the items in the car, updating inventory
- Passwords hashing

## Limitations

- No payment managment (front-end)
- Shopping cart stored in SessionStorage
- API not secure

## Feature Ideas

- Email confirmation
- Order information
- Payment managment
- Security
- Make it responsive

## Challanges

- JPA data relationships, eslecially @ManyToMany
- Front-end testing is time consuming, must use automation tools such as Selenium

## Getting Started

1. Using git:

- Start in a directory where to want to clone this repository
- Execute `git clone https://github.com/ykoziy/NullPointerException_p2.git`
- Switch into newly created directory `NullPointerException_p2`
- Switch into `back-end`
- In console do `mvn clean package -Ddb_user=username -Ddb_url=JDBCurl -Ddb_pass=password`
- This will build and run the tests
- Test report located in: `target\site\jacoco\index.html`
- Executable jar located at: `target\back-end-0.0.1-SNAPSHOT.jar`

2. Docker (contained enviroment to run the back end)

- In the root directory project already has Dockerfile
- Build docker image: `docker build . -t some_image_name`
- Run docker image: `docker run -d -p 8080:8080 -e db_user="user name" -e db_pass="db password" -e db_url="db url" some_image_name`
