import React, { useState } from "react";
import { Button, Card, Form } from "react-bootstrap";
import { Auth } from "aws-amplify";
import "./SignUp.css";

export default function SignUp(props) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  function validateForm() {
    return email.length > 0 && password.length > 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();

    setIsLoading(true);
  
    try {
      await Auth.signUp({username: email, password: password}).then(data => {
        props.history.push("/login");
      }).catch(err => console.log(err));
    } catch (e) {
      console.log(e.message);
      setIsLoading(false);
    }
  }

  return (
    <div className="SignUp">
      <Form onSubmit={handleSubmit}>
        <Card><Card.Body>
        <Card.Title>Sign Up</Card.Title>
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
        <Button block bsSize="large" disabled={!validateForm() || isLoading} type="submit">
          {isLoading? 'Loading…' : 'Login'}
        </Button></Card.Body>
      </Card>
      </Form>
    </div>
  );
}