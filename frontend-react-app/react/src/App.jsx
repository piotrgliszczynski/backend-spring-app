import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css'
import AddUpdateForm from './components/AddUpdateForm'
import CustomerList from './components/CustomerList'
import Heading from './components/Heading';
import { CustomerProvider } from './components/hooks/CustomerContext'
import { DataProvider } from './components/hooks/DataProviderContext';
import LoginForm from './components/LoginForm';
import RequireAuth from './components/RequireAuth';
import { AuthProvider } from './components/hooks/AuthContext';
import RegisterForm from './components/RegisterForm';
import EventList from './components/EventList';
import EventForm from './components/EventForm';

function App() {

  return (
    <>
      <BrowserRouter>
        <AuthProvider>
          <CustomerProvider>
            <DataProvider>
              <Heading />
              <Routes>
                <Route exact path="/" element={
                  <RequireAuth>
                    <CustomerList />
                  </RequireAuth>} />
                <Route path="/customer-form" element={
                  <RequireAuth>
                    <AddUpdateForm />
                  </RequireAuth>
                } />
                <Route path="/login" element={<LoginForm />} />
                <Route path="/register" element={<RegisterForm />} />
                <Route path="/events" element={
                  <RequireAuth>
                    <EventList />
                  </RequireAuth>
                } />
                <Route path="/event-form" element={
                  <RequireAuth>
                    <EventForm />
                  </RequireAuth>
                } />
              </Routes>
            </DataProvider>
          </CustomerProvider>
        </AuthProvider>
      </BrowserRouter>
    </>
  )
}

export default App
