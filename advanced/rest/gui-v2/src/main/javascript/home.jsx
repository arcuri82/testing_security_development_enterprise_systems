import React from "react";
import {Link} from 'react-router-dom';


export class Home extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            books: null,
            error: null
        };

        this.deleteBook = this.deleteBook.bind(this);
    }


    componentDidMount() {
        this.fetchBooks();
    }

    async fetchBooks() {

        const url = "/api/books";

        let response;
        let payload;

        try {
            response = await fetch(url);
            payload = await response.json();
        } catch (err) {
            //Network error: eg, wrong URL, no internet, etc.
            this.setState({
                error: "ERROR when retrieving list of books: " + err,
                books: null
            });
            return;
        }

        if (response.status === 200) {
            this.setState({
                error: null,
                books: payload
            });
        } else {
            this.setState({
                error: "Issue with HTTP connection: status code " + response.status,
                books: null
            });
        }
    }

    async deleteBook(id) {

        const url = "/api/books/" + id;

        let response;

        try {
            response = await fetch(url, {method: "delete"});
        } catch (err) {
            alert("Delete operation failed: " + err);
            return false;
        }

        if (response.status !== 204) {
            alert("Delete operation failed: status code " + response.status);
            return false;
        }

        this.fetchBooks();

        return true;
    }

    render() {

        let table;

        if (this.state.error !== null) {
            table = <p>{this.state.error}</p>
        } else if (this.state.books === null || this.state.books.length === 0) {
            table = <p>There is no book registered in the database</p>
        } else {
            table = <div>
                <table className="allBooks">
                    <thead>
                    <tr>
                        <th>Author(s)</th>
                        <th>Title</th>
                        <th>Year</th>
                        <th>Options</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.books.map(b =>
                        <tr key={"key_" + b.id} className="oneBook" >
                            <td>{b.author}</td>
                            <td>{b.title}</td>
                            <td>{b.year}</td>
                            <td>
                                <Link to={"/edit?bookId=" + b.id}>
                                    <button className="btn" id={"home_edit_btn_" + b.id}>
                                        <i className="fas fa-edit"></i>
                                    </button>
                                </Link>
                                <button className="btn"
                                        id={"home_delete_btn_" + b.id}
                                        onClick={_ => this.deleteBook(b.id)}>
                                    <i className="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        }

        return (
            <div>
                <h2>Book List</h2>
                <Link to={"/create"} id={"home_create_btn"}>
                    <button className="btn">New</button>
                </Link>
                {table}
            </div>
        );
    }
}