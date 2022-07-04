
import React, { useState, useRef, useEffect } from 'react';
import TodoList from './TodoList'
import {v4 as uuidv4} from 'uuid';

const LOCAL_STORAGE_KEY = 'todoApp.todos'

function App() {
  const [todos, setTodos] = useState([]) // start off with an empty array of todos, using object destructuring to split todos and function to update them
  const todoNameRef = useRef()

  // load todos
  useEffect(() => {
    const storedTodos = JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY))
    if (storedTodos) setTodos(storedTodos)
  }, [])

  // save todos
  useEffect(() => {
    localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(todos))
  }, [todos])


  function toggleTodo(id) {
    const newTodos = [...todos] // never directly modify a state variable (creating a copy)

    const todo = newTodos.find(todo => todo.id === id) 
    todo.complete = !todo.complete

    setTodos(newTodos)

  }

  function handleClearTodos() {
    const newTodos = todos.filter(todo => !todo.complete)

    setTodos(newTodos)
  }

  function handleAddTodo(e) {
    const name = todoNameRef.current.value
    if (name === '') return // check for invalid input

    // update the todo's list
    setTodos(prevTodos => {
      return [...prevTodos, {id:uuidv4(),  name: name, complete:false}]
    })

    // clear our input fieldnp
    todoNameRef.current.value = null
  }
  
  return (
    <>
      <TodoList todos={todos} toggleTodo={toggleTodo}/>
      <input ref={todoNameRef} type="text" />
      <button onClick={handleAddTodo}>Add Todo</button>
      <button onClick={handleClearTodos}>Clear Completed Todos</button>
      <div>{todos.filter(todo => !todo.complete).length} left to do</div>
    </>
  )
}

export default App;
