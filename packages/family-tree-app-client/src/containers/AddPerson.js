import React from "react";
import { API } from "aws-amplify";
import { Form, Button } from "react-bootstrap";

export default class ListPersons extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            relations: [],
            firstName: null,
            lastName: null,
            phoneNumber: null,
            work: null,
            birthDate: null,
            relationToUser: null
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        console.log(this.props);
        this.getRelations();
    };

    async getRelations() {
        try {
            const relations = (await API.get("family-tree", "get-relations"));
            this.setState({ relations: relations });
        }
        catch (e) {
            console.log(e);
        }
    }

    handleChange(event) {
        let v = {};
        v[event.target.name] = event.target.value;
        this.setState(v);
        console.log(v);
    }


    async handleSubmit(event) {
        event.preventDefault();
        console.log(event);
        let myInit = {
            body: {
                userId: this.props.userId,
                firstName: this.state.firstName,
                lastName: this.state.lastName,
                phoneNumber: this.state.phoneNumber,
                work: this.state.work,
                birthDate: this.state.birthDate,
                relationToUser: this.state.relationToUser
            }
        }

        await API.post("family-tree", "create-person", myInit);

        this.props.history.push("/persons");
    }

    render() {
        return (
            <div className="AddPerson">
                <h1>Person</h1>
                <Form onSubmit={this.handleSubmit}>
                    <Form.Group controlId="formFirstName">
                        <Form.Label>Vorname</Form.Label>
                        <Form.Control name="firstName" onChange={this.handleChange} />
                    </Form.Group>
                    <Form.Group controlId="formLastName">
                        <Form.Label>Nachname</Form.Label>
                        <Form.Control name="lastName" onChange={this.handleChange} />
                    </Form.Group>
                    <Form.Group controlId="formPhoneNumber">
                        <Form.Label >Telefonnummer</Form.Label>
                        <Form.Control name="phoneNumber" onChange={this.handleChange} />
                    </Form.Group>
                    <Form.Group controlId="formWork">
                        <Form.Label >Arbeit</Form.Label>
                        <Form.Control name="work" onChange={this.handleChange} />
                    </Form.Group>
                    <Form.Group controlId="formBirthDate">
                        <Form.Label >Geburtstag (yyyy-MM-dd)</Form.Label>
                        <Form.Control name="birthDate" onChange={this.handleChange} />
                    </Form.Group>
                    <Form.Group controlId="formRelationship">
                        <Form.Label >Verwandschaftsbeziehung</Form.Label>
                        <Form.Control as="select" name="relationToUser" onChange={this.handleChange}>
                            <option>Bitte auswählen</option>
                            {this.state.relations.map(r => <option>{r}</option>)}
                        </Form.Control>
                    </Form.Group>
                    <Button variant="primary" type="submit">
                        Erstellen
                    </Button>
                </Form>
            </div>
        );
    }
}