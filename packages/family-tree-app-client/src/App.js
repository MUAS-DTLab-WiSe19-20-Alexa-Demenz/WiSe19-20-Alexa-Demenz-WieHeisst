import React, { useState, useEffect } from "react";
import Routes from "./Routes";
import { Auth } from "aws-amplify";
import { Navbar, Nav } from "react-bootstrap"
import { withRouter } from "react-router-dom";
import './App.css';


function App(props) {
  const [isAuthenticating, setIsAuthenticating] = useState(true);
  const [isAuthenticated, userHasAuthenticated] = useState(false);
  const [userId, setUserId] = useState(true);

  useEffect(() => {
    onLoad();
  }, []);

  async function onLoad() {
    try {
      await Auth.currentSession();
      userHasAuthenticated(true);
    }
    catch (e) {
      if (e !== 'No current user') {
        //alert(e);
        console.log(e);
      }
    }

    setIsAuthenticating(false);
  }

  async function handleLogout() {
    await Auth.signOut();

    userHasAuthenticated(false);

    props.history.push("/login");
  }

  return (
    !isAuthenticating &&
    <div className="App container">
      <Navbar bg="light" expand="lg">
        <Navbar.Brand href="/">Family Tree</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse className="justify-content-end">
          <Nav>
            {!isAuthenticated ? <>
              <Nav.Item>
                <Nav.Link href="/sign-up">Signup</Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link href="/login">Login</Nav.Link>
              </Nav.Item></> :
              <Nav.Item onClick={handleLogout}>Logout</Nav.Item>}
          </Nav>
        </Navbar.Collapse>
      </Navbar>
      <input onChange={e => setUserId(e.target.value)} />
      <Routes appProps={{ isAuthenticated, userHasAuthenticated, userId }} />
    </div>
  );
}

export default withRouter(App);
