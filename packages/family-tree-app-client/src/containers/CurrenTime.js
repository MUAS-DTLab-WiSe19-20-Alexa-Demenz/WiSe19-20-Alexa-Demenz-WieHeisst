import React, { useState, useEffect } from "react";
import { API } from "aws-amplify";


export default function CurrentTime() {
  const [currentTime, setCurrentTime] = useState("");

  useEffect(() => {
    onLoad();
  }, []);

  async function onLoad() {
    try {
      const time = (await API.get("family-tree", "ping")).message;
      setCurrentTime(time);
    }
    catch(e) {
      console.log(e);
    }
  }
  return (
    <div className="Current Time">
        <h1>Current Time</h1>
        <p>{currentTime}</p>
    </div>
  );
}