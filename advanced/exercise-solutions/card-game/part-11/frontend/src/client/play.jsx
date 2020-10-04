import React from "react";
import {withRouter} from "react-router-dom";

export class Play extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <p>
                Unfortunately, this functionality is not available yet... :(
            </p>
        )
    }
}

export default withRouter(Play);