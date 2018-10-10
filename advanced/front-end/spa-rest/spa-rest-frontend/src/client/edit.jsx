import React from "react";
import Book from "./book";

export class Edit extends React.Component{

    constructor(props){
        super(props);

        this.state = {
            book: null,
            error: null
        };

        this.onOk = this.onOk.bind(this);
        this.bookId = new URLSearchParams(window.location.search).get("bookId");

        if(this.bookId === null){
            this.state.error = "Unspecified book id";
        }
    }

    componentDidMount(){
        if(this.state.error === null) {
            this.fetchBook();
        }
    }

    async fetchBook(){

        const url = "http://localhost:8081/books/" + this.bookId;

        let response;
        let payload;

        try {
            response = await fetch(url);
            payload = await response.json();
        } catch (err) {
            //Network error: eg, wrong URL, no internet, etc.
            this.setState({
                error: "ERROR when retrieving book: " + err,
                book: null
            });
            return;
        }

        if (response.status === 200) {
            this.setState({
                error: null,
                book: payload.data
            });
        } else {
            this.setState({
                error: "Issue with HTTP connection: status code " + response.status,
                book: null
            });
        }
    }


    async onOk(author, title, year, id){

        const url = "http://localhost:8081/books/"+id;

        const payload = {id, author, title, year};

        let response;

        try {
            response = await fetch(url, {
                method: "put",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });
        } catch (err) {
            return false;
        }

        return response.status === 204;
    }


    render(){

        if(this.state.error !== null){
            return(
                <div>
                    <p>Cannot edit book. {this.state.error}</p>
                </div>
            );
        }

        if(this.state.book === null){
            return(<p>Loading...</p>);
        }

        return(
            <div>
                <h3>Edit Book</h3>
                <Book
                    author={this.state.book.author}
                    title={this.state.book.title}
                    year={this.state.book.year}
                    bookId={this.bookId}
                    ok={"Update"}
                    okCallback={this.onOk}
                />
            </div>
        );
    }
}