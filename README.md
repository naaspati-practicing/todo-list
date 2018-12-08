## todo-list 

Practice of [spark-intercooler](spark-intercooler) (incomplete)

    // returns all todo as json
    http http://localhost:8080/all
               
    // delete todo with id=1
    http delete http://localhost:8080/todos/1   

    // change status of todo[id=2] to completed
    http post http://localhost:8080/todos/2 completed=true
  
    // add a new todo to the list 
    http put http://localhost:8080/todos title=title content=content  