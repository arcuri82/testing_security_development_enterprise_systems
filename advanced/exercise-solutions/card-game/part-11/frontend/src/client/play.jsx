import React from "react";
import {withRouter} from "react-router-dom";

export class Play extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <h2 className="center big-text">
                Unfortunately, this functionality is not available yet... &#128546;
            </h2>
        )
    }
}

export default withRouter(Play);