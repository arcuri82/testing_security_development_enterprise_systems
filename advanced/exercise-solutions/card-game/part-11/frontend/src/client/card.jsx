import React from "react";


const Card = (props) =>{

    let label = props.name;
    if(props.quantity){
        label += " ("+props.quantity+")"
    }

    return(
        <div className="card">
            <h3>{label}</h3>
            <img className="card-image" src={"/api/cards/imgs/"+props.imageId}/>
        </div>
    );
};

export default Card;