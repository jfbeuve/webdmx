class AjaxButton extends React.Component {
  constructor() {
    super();
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick() {
    $.ajax({
      url: this.props.url,
      dataType: 'text',
      cache: false,
      success: function(data) {
      	console.log('GET '+this.props.url);
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  }
  render() {
    return (
        <button type="button" onClick={this.handleClick}>{this.props.name}</button>
    );
  }
}

ReactDOM.render(
	<div>
		<AjaxButton name="show" url="/show/run"></AjaxButton>
		<AjaxButton name="strob" url="/front/strob"></AjaxButton>
		<AjaxButton name="blackout" url="/show/blackout"></AjaxButton>
		<br/>
		<AjaxButton name="tap" url="/show/tap"></AjaxButton>
    </div>
  ,
  document.getElementById('container')
);