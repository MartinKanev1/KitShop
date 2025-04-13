import React from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import MyOrdersList from '../components/MyOrdersList';
import '../Styles/Home.css';


const MyOrders = () => {

   


return (
    <>

    <Header showButtons = {true} />

    <div style={{
  backgroundColor: 'lightblue',
  padding: '20px',
  textAlign: 'center',
  margin: '20px auto',
  marginTop: '100px', 
  maxWidth: '600px',
  borderRadius: '10px'
}}>
  <p style={{ margin: 0 }}>
    Here you can view and manage all of your current orders. You can cancel any pending order below only if the order status is still Pending. If not make sure to contact our support email so we figure out a change or cancelation of an order.
  </p>
</div>

  
    <MyOrdersList />

    <Footer/>

    </>
 )

}

export default MyOrders;