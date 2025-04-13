import React from 'react';


const Footer = ({ showFooter = true }) => {
  if (!showFooter) return null;

  return (
    <footer
      className="footer" 
      style={{ 
        width: '100vw', 
        maxWidth: '100%', 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'center', 
        padding: '10px 20px', 
        backgroundColor: 'lightblue', 
        color: 'white',
        position: 'fixed',
        bottom: 0,
        left: 0,
        boxSizing: 'border-box',
        overflow: 'hidden'
      }}
    >
       <p>📧 Email: support@footykits.com</p>
       <p>📞 Phone: +1 234 567 890</p>
    </footer>
  );
};

export default Footer;