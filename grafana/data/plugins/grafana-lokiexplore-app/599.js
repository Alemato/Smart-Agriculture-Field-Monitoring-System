"use strict";(self.webpackChunkgrafana_lokiexplore_app=self.webpackChunkgrafana_lokiexplore_app||[]).push([[599],{4599:(e,t,r)=>{r.r(t),r.d(t,{default:()=>O,updatePlugin:()=>b});var n=r(8531),o=r(7781),a=r(1269),i=r(6089),l=r(2007),c=r(5959),s=r.n(c),u=r(3241),f=r(2871);function p(e,t,r,n,o,a,i){try{var l=e[a](i),c=l.value}catch(e){return void r(e)}l.done?t(c):Promise.resolve(c).then(n,o)}function d(e){return function(){var t=this,r=arguments;return new Promise((function(n,o){var a=e.apply(t,r);function i(e){p(a,n,o,i,l,"next",e)}function l(e){p(a,n,o,i,l,"throw",e)}i(void 0)}))}}function g(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}const y=e=>({colorWeak:i.css`
    color: ${e.colors.text.secondary};
  `,marginTop:i.css`
    margin-top: ${e.spacing(3)};
  `,marginTopXl:i.css`
    margin-top: ${e.spacing(6)};
  `,label:(0,i.css)({display:"flex",alignItems:"center",marginBottom:e.spacing(.75)}),icon:(0,i.css)({marginLeft:e.spacing(1)})}),m=function(){var e=d((function*(e,t){try{yield b(e,t),n.locationService.reload()}catch(e){f.v.error(e,{msg:"Error while updating the plugin"})}}));return function(t,r){return e.apply(this,arguments)}}(),v={container:"data-testid ac-container",interval:"data-testid ac-interval-input",submit:"data-testid ac-submit-form"},b=function(){var e=d((function*(e,t){const r=(0,n.getBackendSrv)().fetch({url:`/api/plugins/${e}/settings`,method:"POST",data:t});return(yield(0,a.lastValueFrom)(r)).data}));return function(t,r){return e.apply(this,arguments)}}(),h=e=>{try{if(e){const t=o.rangeUtil.intervalToSeconds(e);return(0,u.isNumber)(t)&&t>=3600}return!0}catch(e){}return!1},O=({plugin:e})=>{const t=(0,l.useStyles2)(y),{enabled:r,pinned:n,jsonData:o}=e.meta;var a,i;const[u,f]=(0,c.useState)({interval:null!==(a=null==o?void 0:o.interval)&&void 0!==a?a:"",isValid:h(null!==(i=null==o?void 0:o.interval)&&void 0!==i?i:"")});return s().createElement("div",{"data-testid":v.container},s().createElement(l.FieldSet,{label:"Settings"},s().createElement(l.Field,{invalid:!h(u.interval),error:'Interval is invalid. Please enter an interval longer then "60m". For example: 3d, 1w, 1m',description:s().createElement("span",null,"The maximum interval that can be selected in the time picker within the Explore Logs app. If empty, users can select any time range interval in Explore Logs. ",s().createElement("br",null),"Example values: 7d, 24h, 2w"),label:"Maximum time picker interval",className:t.marginTop},s().createElement(l.Input,{width:60,id:"interval","data-testid":v.interval,label:"Max interval",value:null==u?void 0:u.interval,placeholder:"7d",onChange:e=>{const t=e.target.value.trim();var r,n;f((r=function(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{},n=Object.keys(r);"function"==typeof Object.getOwnPropertySymbols&&(n=n.concat(Object.getOwnPropertySymbols(r).filter((function(e){return Object.getOwnPropertyDescriptor(r,e).enumerable})))),n.forEach((function(t){g(e,t,r[t])}))}return e}({},u),n=null!=(n={interval:t,isValid:h(t)})?n:{},Object.getOwnPropertyDescriptors?Object.defineProperties(r,Object.getOwnPropertyDescriptors(n)):function(e){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t.push.apply(t,r)}return t}(Object(n)).forEach((function(e){Object.defineProperty(r,e,Object.getOwnPropertyDescriptor(n,e))})),r))}})),s().createElement("div",{className:t.marginTop},s().createElement(l.Button,{type:"submit","data-testid":v.submit,onClick:()=>m(e.meta.id,{enabled:r,pinned:n,jsonData:{interval:u.interval}}),disabled:!h(u.interval)},"Save settings"))))}},2871:(e,t,r)=>{r.d(t,{v:()=>s});var n=r(8531),o=r(2533),a=r(4011);function i(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function l(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{},n=Object.keys(r);"function"==typeof Object.getOwnPropertySymbols&&(n=n.concat(Object.getOwnPropertySymbols(r).filter((function(e){return Object.getOwnPropertyDescriptor(r,e).enumerable})))),n.forEach((function(t){i(e,t,r[t])}))}return e}const c={app:o.id,version:"1.0.5"},s={info:(e,t)=>{const r=l({},c,t);console.log(e,r),u(e,r)},warn:(e,t)=>{const r=l({},c,t);console.warn(e,r),f(e,r)},error:(e,t)=>{const r=l({},c,t);console.error(e,r),p(e,r)}},u=(e,t)=>{try{(0,n.logInfo)(e,t)}catch(e){console.warn("Failed to log faro event!")}},f=(e,t)=>{try{(0,n.logWarning)(e,t)}catch(r){console.warn("Failed to log faro warning!",{msg:e,context:t})}},p=(e,t)=>{let r=t;try{!function(e,t){if("object"==typeof e&&null!==e&&((0,a.u4)(e)&&Object.keys(e).forEach((r=>{const n=e[r];"string"!=typeof n&&"boolean"!=typeof n&&"number"!=typeof n||(t[r]=n.toString())})),d(e)))if("object"==typeof e.data&&null!==e.data)try{t.data=JSON.stringify(e.data)}catch(e){}else"string"!=typeof e.data&&"boolean"!=typeof e.data&&"number"!=typeof e.data||(t.data=e.data.toString())}(e,r),e instanceof Error?(0,n.logError)(e,r):"string"==typeof e?(0,n.logError)(new Error(e),r):e&&"object"==typeof e?r.msg?(0,n.logError)(new Error(r.msg),r):(0,n.logError)(new Error("error object"),r):(0,n.logError)(new Error("unknown error"),r)}catch(t){console.error("Failed to log faro error!",{err:e,context:r})}},d=e=>"data"in e},4011:(e,t,r)=>{r.d(t,{FH:()=>f,OK:()=>g,QX:()=>y,Zt:()=>d,aJ:()=>c,fS:()=>p,lb:()=>s,u4:()=>l,v_:()=>u});var n=r(7781);const o=e=>"object"==typeof e&&null!==e;function a(e,t){return t in e}const i=e=>"string"==typeof e&&e||"",l=e=>"object"==typeof e;function c(e){let t=[];if(Array.isArray(e))for(let r=0;r<e.length;r++)t.push(i(e[r]));return t}function s(e){const t=o(e)&&a(e,"row")&&a(e,"id")&&e;if(t){const e="number"==typeof t.row&&t.row,r="string"==typeof t.id&&t.id;if(!1!==r&&!1!==e)return{row:e,id:r}}return!1}function u(e){return"string"==typeof e&&("logs"===e||"table"===e)&&e}function f(e){return"string"==typeof e&&e===n.LogsSortOrder.Ascending.toString()?n.LogsSortOrder.Ascending:"string"==typeof e&&e===n.LogsSortOrder.Descending.toString()&&n.LogsSortOrder.Descending}function p(e){const t=o(e)&&a(e,"value")&&a(e,"parser")&&e;if(t){const e="string"==typeof t.parser&&("logfmt"===t.parser||"json"===t.parser||"mixed"===t.parser||"structuredMetadata"===t.parser)&&t.parser,r="string"==typeof t.value&&t.value;if(!1!==e&&!1!==r)return{parser:e,value:r}}return!1}function d(e){const t=o(e)&&l(e)&&e;if(t){const e=Object.keys(t),r={};for(let n=0;n<e.length;n++){const o=e[n],a=t[e[n]];"number"==typeof a&&(r[o]=a)}return r}return!1}function g(e){const t=o(e)&&a(e,"to")&&a(e,"from")&&e;if(t){const e=i(t.to),r=i(t.from);if(e&&r)return{to:e,from:r}}}class y extends Error{}}}]);
//# sourceMappingURL=599.js.map