import React from "react";
import {Link, withRouter} from 'react-router-dom'


class Book extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            author: this.props.author ? this.props.author : "",
            title: this.props.title ? this.props.title : "",
            year: this.props.year ? this.props.year : ""
        };

        this.ok = this.props.ok ? this.props.ok : "Ok";

        this.onFormSubmit = this.onFormSubmit.bind(this);
        this.onAuthorChange = this.onAuthorChange.bind(this);
        this.onTitleChange = this.onTitleChange.bind(this);
        this.onYearChange = this.onYearChange.bind(this);
    }

    async onFormSubmit(event) {
        event.preventDefault();

        const completed = await this.props.okCallback(
            this.state.author,
            this.state.title,
            this.state.year,
            this.props.bookId);

        if(completed) {
            this.props.history.push('/');
        } else {
            alert("Failed to create new Book")
        }
    }

    onAuthorChange(event) {
        this.setState({author: event.target.value});
    }

    onTitleChange(event) {
        this.setState({title: event.target.value});
    }

    onYearChange(event) {
        this.setState({year: event.target.value});
    }

    render() {

        return (
            <div>
                <form onSubmit={this.onFormSubmit}>
                    <div className="inputTitle">Author(s):</div>
                    <input
                        placeholder={"Type the author(s) of this book"}
                        value={this.state.author}
                        onChange={this.onAuthorChange} 
                        className="bookInput"
                        id={"author_input"}
                    />
                    <div className="inputTitle">Title:</div>
                    <input
                        placeholder={"Type the title of this book"}
                        value={this.state.title}
                        onChange={this.onTitleChange}
                        className="bookInput"
                        id={"title_input"}
                    />
                    <div className="inputTitle">Year:</div>
                    <input
                        placeholder={"Type the year in which this book was published"}
                        value={this.state.year}
                        onChange={this.onYearChange}
                        className="bookInput"
                        id={"year_input"}
                    />

                    <button type="submit" className={"btn"} id={"submit_btn"}>{this.ok}</button>
                    <Link to={"/"}><button className={"btn"} id={"cancel_btn"}>Cancel</button></Link>
                </form>
            </div>
        );
    }
}


/*
    Needed, because otherwise this.props.history would be undefined
 */
export default withRouter(Book);