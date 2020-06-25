import React, { useState } from "react";
import { Button, Card, Form, Alert } from "react-bootstrap";
import { Auth } from "aws-amplify";
import "./Login.css";

export default function Login(props) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [notYetConfirmed, setNotYetConfirmed] = useState(false);

  function validateForm() {
    return email.length > 0 && password.length > 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();

    setIsLoading(true);

    try {
      const user = await Auth.signIn(email, password);
      if (user.challengeName === 'NEW_PASSWORD_REQUIRED') {
        props.history.push("/new-password");
      } else {
        props.userHasAuthenticated(true);
        props.history.push("/persons");
      }
    } catch (e) {
      if (e.code === 'UserNotConfirmedException') {
        setNotYetConfirmed(true);
      }
      console.log(e.message);
      setIsLoading(false);
    }
  }

  return (
    <div className="Login">
      <Form onSubmit={handleSubmit}>
        <Card><Card.Body>
          <Form.Group controlId="email" bsSize="large">
            <Form.Label>Email</Form.Label>
            <Form.Control
              autoFocus
              type="email"
              value={email}
              onChange={e => setEmail(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="password" bsSize="large">
            <Form.Label>Password</Form.Label>
            <Form.Control
              value={password}
              onChange={e => setPassword(e.target.value)}
              type="password"
            />
          </Form.Group>
          {notYetConfirmed ? <Alert variant={"danger"}>You are not yet confirmed!</Alert> : null}
          <Button block bsSize="large" disabled={!validateForm() || isLoading} type="submit">
            {isLoading ? 'Loading…' : 'Login'}
          </Button></Card.Body>
        </Card>
      </Form>
    </div>
  );
}