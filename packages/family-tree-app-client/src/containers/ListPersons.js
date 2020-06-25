import React from "react";
import { API } from "aws-amplify";
import { Button } from "react-bootstrap";
import BootstrapTable from 'react-bootstrap-table-next';


export default class ListPersons extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            persons: [],
            personToDelete: ""
        };
        this.deletePerson = this.deletePerson.bind(this);
    }

    componentDidMount() {
        this.getPersons();
    };

    async getPersons() {
        try {
            const persons = (await API.get("family-tree", "get-persons?userId=" + this.props.userId));
            this.setState({ persons: persons });
        }
        catch (e) {
            console.log(e);
        }
    }

    async deletePerson() {
        try {
            await API.del("family-tree", "delete-person", { body: { personId: this.state.personToDelete, userId: this.props.userId } });
            this.getPersons();
        }
        catch (e) {
            console.log(e);
        }
    }

    render() {
        const columns = [{
            dataField: 'firstName',
            text: 'Vorname'
        }, {
            dataField: 'lastName',
            text: 'Nachname'
        }, {
            dataField: 'phoneNumber',
            text: 'Telefon'
        }, {
            dataField: 'work',
            text: 'Arbeit'
        }, {
            dataField: 'hobbies',
            text: 'Hobbies'
        }, {
            dataField: 'birthDate',
            text: 'Geburtstag'
        }, {
            dataField: 'relationToUser',
            text: 'Beziehung'
        }];
        const selectRow = {
            mode: 'radio',
            clickToSelect: true,
            onSelect: (row) => {
                this.setState({ personToDelete: row.personId });
                console.log(row.personId);
            }
        };
        return (
            <div className="Persons">
                <h1>Persons</h1>
                <Button variant="danger" style={{ float: "left" }} onClick={this.deletePerson}>Löschen</Button>
                <Button variant="success" style={{ float: "right" }} onClick={() => this.props.history.push("/add-person")}>+</Button>
                <Button variant="primary" style={{ float: "right" }} onClick={() => this.getPersons()}>⟳</Button>
                <BootstrapTable keyField='personId' data={this.state.persons} columns={columns} selectRow={selectRow} />
            </div>
        );
    }
}