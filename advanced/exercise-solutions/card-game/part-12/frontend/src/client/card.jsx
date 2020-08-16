import React from "react";


const Card = (props) =>{

    return(
        <div>
            <p>{props.name + " ("+props.quantity+")"}</p>
            <img src={"/api/cards/imgs/"+props.imageId}/>
        </div>
    );
};

export default Card;