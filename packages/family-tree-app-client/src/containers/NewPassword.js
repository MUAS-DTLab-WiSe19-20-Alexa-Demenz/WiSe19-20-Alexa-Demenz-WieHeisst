import React, { useState } from "react";
import { Button, Card, Form } from "react-bootstrap";
import { Auth } from "aws-amplify";
import "./NewPassword.css";

export default function NewPassword(props) {
  const [email, setEmail] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  function validateForm() {
    return email.length > 0 && oldPassword.length > 0 && newPassword.length > 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();

    setIsLoading(true);
  
    try {
      const user = await Auth.signIn(email, oldPassword);
      if (user.challengeName === 'NEW_PASSWORD_REQUIRED') {
        await Auth.completeNewPassword(user, newPassword);
        props.userHasAuthenticated(true);
        props.history.push("/");
      } else {
        props.userHasAuthenticated(true);
        props.history.push("/");
      }
    } catch (e) {
      console.log(e.message);
      setIsLoading(false);
    }
  }

  return (
    <div className="NewPassword">
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
        <Form.Group controlId="old-password" bsSize="large">
          <Form.Label>Old Password</Form.Label>
          <Form.Control
            value={oldPassword}
            onChange={e => setOldPassword(e.target.value)}
            type="password"
          />
        </Form.Group>
        <Form.Group controlId="new-password" bsSize="large">
          <Form.Label>New Password</Form.Label>
          <Form.Control
            value={newPassword}
            onChange={e => setNewPassword(e.target.value)}
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