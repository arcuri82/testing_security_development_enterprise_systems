import React from "react";
import Book from "./book";

export class Create extends React.Component{

    constructor(props){
        super(props);

        this.onOk = this.onOk.bind(this);
    }

    async onOk(author, title, year, bookId){

        const url = "http://localhost:8081/books";

        //note: here bookId is ignored
        const payload = {author, title, year};

        let response;

        try {
            response = await fetch(url, {
                method: "post",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });
        } catch (err) {
            return false;
        }

        return response.status === 201;
    }


    render(){

        return(
            <div>
                <h3>Create a New Book</h3>
                <Book
                    author={""}
                    title={""}
                    year={""}
                    ok={"Create"}
                    okCallback={this.onOk}
                />
            </div>
        );
    }
}